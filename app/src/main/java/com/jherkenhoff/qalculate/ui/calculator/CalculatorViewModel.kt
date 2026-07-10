package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jherkenhoff.qalculate.data.AutocompleteRepository
import com.jherkenhoff.qalculate.data.CalculatorRepository
import com.jherkenhoff.qalculate.data.UserPreferencesRepository
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import com.jherkenhoff.qalculate.data.repository.CalculationHistoryStore
import com.jherkenhoff.qalculate.domain.AutocompleteResult
import com.jherkenhoff.qalculate.domain.AutocompleteUseCase
import com.jherkenhoff.qalculate.domain.CalculateUseCase
import com.jherkenhoff.qalculate.domain.ParseUseCase
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.CalculatorAction
import com.jherkenhoff.qalculate.model.UndoManager
import com.jherkenhoff.qalculate.model.UserPreferences
import com.jherkenhoff.qalculate.ui.common.mathExpressionPlainText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.math.absoluteValue

data class InternalTextFieldValue(
    val textFieldValue: TextFieldValue,
    val doAutocomplete: Boolean
)

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val parseUseCase: ParseUseCase,
    private val calculateUseCase: CalculateUseCase,
    private val autocompleteUseCase: AutocompleteUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val autocompleteRepository: AutocompleteRepository,
    private val calculationHistoryStore: CalculationHistoryStore,
    private val calculatorRepository: CalculatorRepository
) : ViewModel() {

    private val _calculationDisplayOrder = MutableStateFlow<List<Long>>(emptyList())
    private val _activeCalculationId = MutableStateFlow<Long?>(null)
    private val _internalInputTextFieldValue = MutableStateFlow(InternalTextFieldValue(TextFieldValue(), false))

    val persistentCalculationList = calculationHistoryStore.allItemsById().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
    )

    init {
        viewModelScope.launch {
            calculationHistoryStore.allItems().first().let { calculations ->
                _calculationDisplayOrder.value = calculations
                    .sortedBy { it.sortIndex }
                    .map { it.id }
                _activeCalculationId.value = _calculationDisplayOrder.value.lastOrNull()
            }
        }

        persistentCalculationList
            .onEach { calculationHistory ->
                if (calculationHistory.isEmpty()) {
                    calculationHistoryStore.addItem(
                        CalculationHistoryItemData(
                            sortIndex = 0,
                            input = "",
                            parsed = "",
                            result = "",
                            created = LocalDateTime.now(),
                            modified = LocalDateTime.now()
                        )
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    val inputTextFieldValue = _internalInputTextFieldValue.map { it.textFieldValue }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = TextFieldValue()
    )

    val parsedString = combine(inputTextFieldValue, userPreferencesRepository.userPreferencesFlow) { inputTextFieldValue, userPreferences ->
        return@combine parseUseCase(inputTextFieldValue.text, userPreferences)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val resultString = combine(inputTextFieldValue, userPreferencesRepository.userPreferencesFlow) { inputTextFieldValue, userPreferences ->
        return@combine calculateUseCase(inputTextFieldValue.text, userPreferences)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val autocompleteResult = combine(autocompleteRepository.trie , _internalInputTextFieldValue) { autocompleteTrie, internalInputTextFieldValue ->
        if (internalInputTextFieldValue.doAutocomplete)
            autocompleteUseCase(autocompleteTrie, internalInputTextFieldValue.textFieldValue)
        else
            AutocompleteResult()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = AutocompleteResult()
    )

    val userPreferences = userPreferencesRepository.userPreferencesFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserPreferences()
    )

    private val _calculationExecutionOrder = MutableStateFlow<List<Long>>(emptyList())

    val calculationListData = combine(
        persistentCalculationList,
        _calculationDisplayOrder,
        _activeCalculationId,
        _calculationExecutionOrder
    ) { calculations, order, activeId, executionOrder ->

        CalculationListData(
            items = order
                .mapNotNull { id ->
                    calculations[id]
                }
                .map { calculation ->
                    CalculationItem(
                        id = calculation.id,
                        input = calculation.input,
                        parsed = calculation.parsed,
                        result = calculation.result,
                        executionOrderNumber = executionOrder
                            .indexOfLast { it == calculation.id }
                            .takeIf { it != -1 }
                    )
                },
            activeCalculationId = activeId
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = CalculationListData(emptyList(), null)
    )

    val activeKeypadIndex = userPreferences.map{
        it.activeKeypadIndex
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserPreferences().activeKeypadIndex
    )

    private val undoManager = UndoManager<TextFieldValue>()
    val undoState = undoManager.state

    private val _autocompleteDismissed = MutableStateFlow(false)
    val autocompleteDismissed = _autocompleteDismissed.asStateFlow()

    fun setActiveKeypadIndex(i: Int) {
        updateUserPreferences(
            userPreferences.value.copy(activeKeypadIndex = i)
        )
    }

    fun persistActiveCalculation() {
        persistentCalculationList.value[_activeCalculationId.value]?.let { calculation ->
            viewModelScope.launch {
                calculationHistoryStore.updateItem(
                    calculation.copy(
                        input = inputTextFieldValue.value.text,
                        parsed = parsedString.value,
                        result = resultString.value,
                        modified = LocalDateTime.now()
                    )
                )
            }
        }
    }

    fun setActiveCalculationId(id: Long) {
        persistActiveCalculation()

        _activeCalculationId.update { id }

        viewModelScope.launch {
            // HACK: Querying the room DB each time the active calculation is changed might be a minor performance overhead
            // It is currently implemented this way to avoid a race condition after adding a new calculation
            val input = calculationHistoryStore.getItem(id).input

            updateInput(
                TextFieldValue(input, TextRange(input.length))
            )
        }
    }

    fun setActiveCalculationIdx(idx: Int) {
        val id = _calculationDisplayOrder.value[idx]
        setActiveCalculationId(id)
    }

    fun clearCalculationHistory() {
        viewModelScope.launch {
            calculationHistoryStore.deleteAll()
        }
    }

    fun updateUserPreferences(userPreferences: UserPreferences) {
        viewModelScope.launch {
            userPreferencesRepository.updateUserPreferences(userPreferences)
        }
    }

    fun submitCalculation() {
        val currentId = _activeCalculationId.value ?: return

        calculatorRepository.setAnsExpression(mathExpressionPlainText(inputTextFieldValue.value.text))

        // Append a new, blank calculation at the end if the currently submitted calculation was the last
        if (currentId == _calculationDisplayOrder.value.lastOrNull()) {
            viewModelScope.launch {
                val newId = calculationHistoryStore.addItem(
                    CalculationHistoryItemData(
                        sortIndex = 0,
                        input = inputTextFieldValue.value.text,
                        parsed = parsedString.value,
                        result = resultString.value,
                        created = LocalDateTime.now(),
                        modified = LocalDateTime.now()
                    )
                )

                _calculationDisplayOrder.update { order -> order + newId }
                setActiveCalculationId( newId )

                updateInput(
                    inputTextFieldValue.value.copy(selection = TextRange(0, inputTextFieldValue.value.text.length))
                )
            }
        } else {
            val nextIdx = _calculationDisplayOrder.value.indexOfFirst { it == currentId } + 1
            setActiveCalculationIdx(nextIdx)

        }

        undoManager.clear()
    }

    fun reorderCalculation(fromIdx: Int, toIdx: Int) {
        _calculationDisplayOrder.value = _calculationDisplayOrder.value.toMutableList().apply {
            add(toIdx, removeAt(fromIdx))
        }
    }

    fun persistCalculationOrder() {
        viewModelScope.launch {
            _calculationDisplayOrder.value.forEachIndexed { idx, id ->
                calculationHistoryStore.updateSortIndex(id, idx)
            }
        }
    }

    fun deleteCalculation(id: Long) {
        val deletedCalculationOrderIndex = _calculationDisplayOrder.value.indexOfFirst { it == id }.takeIf{ it != -1 }

        persistentCalculationList.value[id]?.let { calculation ->
            _calculationDisplayOrder.update { displayOrder ->
                displayOrder.toMutableList().apply { remove(id) }
            }

            viewModelScope.launch {
                calculationHistoryStore.deleteItem(calculation)
            }
        }
    }

    fun handleKeyAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.InsertText -> insertText(action)
            is CalculatorAction.DeleteChars -> removeChars(action.nChars)
            is CalculatorAction.SubmitCalculation -> submitCalculation()
            is CalculatorAction.ClearAll -> clearInput()
            is CalculatorAction.MoveCursor -> moveCursor(action.nChars)
            is CalculatorAction.TraverseHistory -> traverseHistory(action.nEntries)
            is CalculatorAction.StoreAsVariable -> null
            is CalculatorAction.InsertDecimalSymbol -> insertText(userPreferences.value.getDecimalSeparatorString())
            is CalculatorAction.InsertDivisionSymbol -> insertText(userPreferences.value.getDivisionSignString())
            is CalculatorAction.InsertMultiplicationSymbol -> insertText(userPreferences.value.getMultiplicationSignString())
        }
    }

    fun traverseHistory(nEntries: Int) {
        if (nEntries == -1) undo()
        else if (nEntries == 1) redo()
        else throw IllegalArgumentException("Traversing history by more then one entry is currently not supported.")
    }

    fun undo() {
        val undoState = undoManager.undo(inputTextFieldValue.value)
        if (undoState !== null)
            _internalInputTextFieldValue.update { InternalTextFieldValue(undoState, false) }
    }

    fun redo() {
        val redoState = undoManager.redo(inputTextFieldValue.value)
        if (redoState !== null)
            _internalInputTextFieldValue.update { InternalTextFieldValue(redoState, false) }
    }

    fun moveCursor(chars: Int) {
        val newCursorPosition = (inputTextFieldValue.value.selection.end + chars).coerceIn(0, inputTextFieldValue.value.text.length)
        updateInput(inputTextFieldValue.value.copy(selection = TextRange(newCursorPosition)))
    }

    fun updateInput(input: TextFieldValue, doAutocomplete: Boolean = false) {
        val textChanged = input.text != inputTextFieldValue.value.text
        if (textChanged)
            undoManager.snapshot(inputTextFieldValue.value)

        _internalInputTextFieldValue.update { InternalTextFieldValue(input, doAutocomplete && textChanged) }

        _activeCalculationId.value?.let { id ->
            if (_calculationExecutionOrder.value.lastOrNull() != id) {
                _calculationExecutionOrder.update { it + id }
            }
        }
    }

    fun updateInput(input: String, doAutocomplete: Boolean = false) {
        updateInput(TextFieldValue(input), doAutocomplete)
    }

    fun clearInput() {
        updateInput("")
    }

    fun insertText(action: CalculatorAction.InsertText) {
        val maxChars = inputTextFieldValue.value.text.length
        val textBeforeSelection = inputTextFieldValue.value.getTextBeforeSelection(maxChars)
        val selectedText = inputTextFieldValue.value.getSelectedText()
        val textAfterSelection = inputTextFieldValue.value.getTextAfterSelection(maxChars)

        with(action) {
            if (selectedText.isNotEmpty()) {
                    val newText = when (selectionPolicy) {
                        CalculatorAction.InsertText.SelectionPolicy.REPLACE -> "$textBeforeSelection$preCursorText$postCursorText$textAfterSelection"
                        CalculatorAction.InsertText.SelectionPolicy.SURROUND -> "$textBeforeSelection$preCursorText$selectedText$postCursorText$textAfterSelection"
                        CalculatorAction.InsertText.SelectionPolicy.PARENTHESES -> "$textBeforeSelection($selectedText)$preCursorText$postCursorText$textAfterSelection"
                    }

                    val newSelection = when (selectionPolicy) {
                        CalculatorAction.InsertText.SelectionPolicy.REPLACE -> TextRange(textBeforeSelection.length + preCursorText.length)
                        CalculatorAction.InsertText.SelectionPolicy.SURROUND -> TextRange(textBeforeSelection.length, newText.length - textAfterSelection.length)
                        CalculatorAction.InsertText.SelectionPolicy.PARENTHESES -> TextRange(newText.length - postCursorText.length - textAfterSelection.length)
                    }

                    updateInput(TextFieldValue(
                        text = newText,
                        selection = newSelection
                    ))
            } else {
                updateInput(TextFieldValue(
                    text = "$textBeforeSelection$preCursorText$postCursorText$textAfterSelection",
                    selection = TextRange(textBeforeSelection.length + preCursorText.length)
                ))
            }
        }
    }

    fun replaceRange(range: TextRange, preCursorText: String, postCursorText: String = "") {
        val newText = inputTextFieldValue.value.text.replaceRange(range.start, range.end, "$preCursorText$postCursorText")
        val newCursorPosition = range.start + preCursorText.length

        updateInput(TextFieldValue(
            text = newText,
            selection = TextRange(newCursorPosition)
        ))
    }

    fun insertText(preCursorText: String, postCursorText: String = "") {
        val maxChars = inputTextFieldValue.value.text.length
        val textBeforeSelection = inputTextFieldValue.value.getTextBeforeSelection(maxChars)
        val textAfterSelection = inputTextFieldValue.value.getTextAfterSelection(maxChars)
        val newText = "$textBeforeSelection$preCursorText$postCursorText$textAfterSelection"
        val newCursorPosition = textBeforeSelection.length + preCursorText.length

        updateInput(TextFieldValue(
            text = newText,
            selection = TextRange(newCursorPosition)
        ))
    }

    fun removeChars(nChars: Int) {
        val maxChars = inputTextFieldValue.value.text.length
        var textBeforeSelection = inputTextFieldValue.value.getTextBeforeSelection(maxChars).text
        var textAfterSelection = inputTextFieldValue.value.getTextAfterSelection(maxChars).text

        if (inputTextFieldValue.value.selection.length == 0) {
            if (nChars > 0) {
                textAfterSelection = textAfterSelection.drop(nChars.absoluteValue)
            } else if (nChars < 0) {
                textBeforeSelection = textBeforeSelection.dropLast(nChars.absoluteValue)
            }
        }

        val newText = "$textBeforeSelection$textAfterSelection"
        val newCursorPosition = textBeforeSelection.length

        updateInput(TextFieldValue(
            text = newText,
            selection = TextRange(newCursorPosition)
        ))
    }

    fun acceptAutocomplete(autocompleteItem: AutocompleteItem) {
        replaceRange(
            autocompleteResult.value.contextRange,
            autocompleteItem.typeBeforeCursor,
            autocompleteItem.typeAfterCursor
        )

    }
}