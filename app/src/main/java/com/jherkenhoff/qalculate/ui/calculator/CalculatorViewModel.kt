package com.jherkenhoff.qalculate.ui.calculator

import android.util.Log
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import com.jherkenhoff.qalculate.data.repository.CalculationListRepository
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.math.absoluteValue

data class ActiveCalculationInput(
    val id: Long?,
    val input: TextFieldValue,
    val doAutocomplete: Boolean,
)

data class ActiveCalculationData(
    val id: Long?,
    val input: TextFieldValue,
    val parsed: String,
    val result: String,
    val autocompleteResult: AutocompleteResult
)

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val parseUseCase: ParseUseCase,
    private val calculateUseCase: CalculateUseCase,
    private val autocompleteUseCase: AutocompleteUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val autocompleteRepository: AutocompleteRepository,
    private val calculationListRepository: CalculationListRepository,
    private val calculatorRepository: CalculatorRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            calculationListRepository.ensureNotEmpty()
        }
    }
    private val _calculationDragOrder = MutableStateFlow<List<Long>?>(null)

    private val _activeCalculationInput = MutableStateFlow(ActiveCalculationInput(null, TextFieldValue(), false))

    val persistentCalculationList = calculationListRepository.allItemsSorted().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            persistentCalculationList
                .collect { list ->
                    // Set the active calculation ID if not already set (hopefully only at startup)
                    val activeId = _activeCalculationInput.value.id

                    if (activeId == null || list.none { it.id == activeId }) {
                        list.lastOrNull()?.let { lastCalculation ->
                            changeActiveCalculation(lastCalculation.id)
                        }
                    }
                }
        }
    }

    val userPreferences = userPreferencesRepository.userPreferencesFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserPreferences()
    )

    val activeCalculationData = combine(_activeCalculationInput, userPreferences, autocompleteRepository.trie) { activeCalculationInput, userPreferences, autocompleteTrie ->
        val autocompleteResult = if (activeCalculationInput.doAutocomplete)
            autocompleteUseCase(autocompleteTrie, activeCalculationInput.input)
        else
            AutocompleteResult()

        ActiveCalculationData(
            id = activeCalculationInput.id,
            input = activeCalculationInput.input,
            parsed = parseUseCase(activeCalculationInput.input.text, userPreferences),
            result = calculateUseCase(activeCalculationInput.input.text, userPreferences),
            autocompleteResult = autocompleteResult
        )
    }.onEach {
        it.id?.let { id ->
            if (_calculationExecutionOrder.value.lastOrNull() != id) {
                _calculationExecutionOrder.update { it + id }
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue =
            ActiveCalculationData(
                id = null,
                input = TextFieldValue(""),
                parsed = "",
                result = "",
                autocompleteResult = AutocompleteResult()
            )
    )

    private val _calculationExecutionOrder = MutableStateFlow<List<Long>>(emptyList())

    val calculationListData = combine(
        persistentCalculationList,
        _calculationDragOrder,
        activeCalculationData,
        _calculationExecutionOrder
    ) { calculations, displayOrder, activeCalculationData, executionOrder ->

        val sortedCalculations = if (displayOrder == null) {
            calculations
        } else {
            displayOrder.mapNotNull { id -> calculations.find { it.id == id } }
        }

        CalculationListData(
            items = sortedCalculations.map { calculation ->
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
            activeCalculationId = activeCalculationData.id
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
        persistentCalculationList.value.find { it.id == _activeCalculationInput.value.id } ?.let { calculation ->
            viewModelScope.launch {
                calculationListRepository.updateItem(
                    calculation.copy(
                        input = activeCalculationData.value.input.text,
                        parsed = activeCalculationData.value.parsed,
                        result = activeCalculationData.value.result,
                        modified = LocalDateTime.now()
                    )
                )
            }
        }
    }

    fun changeActiveCalculation(calculation: ActiveCalculationInput) {
        persistActiveCalculation()
        viewModelScope.launch {
            _activeCalculationInput.update { calculation }
        }
    }

    fun changeActiveCalculation(id: Long) {
        persistActiveCalculation()

        viewModelScope.launch {
            // HACK: Querying the room DB each time the active calculation is changed might be a minor performance overhead
            // It is currently implemented this way to avoid a race condition after adding a new calculation
            val input = calculationListRepository.getItem(id).input

            _activeCalculationInput.update {
                ActiveCalculationInput(
                    id = id,
                    input = TextFieldValue(input, TextRange(input.length)),
                    doAutocomplete = true
                )
            }
        }
    }

    fun clearCalculationHistory() {
        viewModelScope.launch {
            calculationListRepository.deleteAll()
        }
    }

    fun updateUserPreferences(userPreferences: UserPreferences) {
        viewModelScope.launch {
            userPreferencesRepository.updateUserPreferences(userPreferences)
        }
    }

    fun submitActiveCalculation() {
        val currentId = activeCalculationData.value.id ?: return

        calculatorRepository.setAnsExpression(mathExpressionPlainText(activeCalculationData.value.input.text))

        // Append a new, blank calculation at the end if the currently submitted calculation was the last
        if (currentId == persistentCalculationList.value.last().id) {
            viewModelScope.launch {
                val newId = calculationListRepository.addItem(
                    CalculationHistoryItemData(
                        sortIndex = 0,
                        input = activeCalculationData.value.input.text,
                        parsed = activeCalculationData.value.parsed,
                        result = activeCalculationData.value.result,
                        created = LocalDateTime.now(),
                        modified = LocalDateTime.now()
                    )
                )

                changeActiveCalculation(
                    ActiveCalculationInput(
                        id = newId,
                        input = activeCalculationData.value.input.copy(selection = TextRange(0, activeCalculationData.value.input.text.length)),
                        doAutocomplete = true
                    )
                )
            }
        } else {
            val nextIdx = persistentCalculationList.value.indexOfFirst { it.id == currentId } + 1
            val nextId = persistentCalculationList.value[nextIdx].id
            changeActiveCalculation(nextId)
        }

        undoManager.clear()
    }

    fun reorderCalculation(fromIdx: Int, toIdx: Int) {
        val currentOrder = _calculationDragOrder.value?: persistentCalculationList.value.map { it.id }

        _calculationDragOrder.update {
            currentOrder.toMutableList().apply {
                add(toIdx, removeAt(fromIdx))
            }
        }
    }

    fun persistCalculationOrder() {
        _calculationDragOrder.value?.let {
            viewModelScope.launch {
                calculationListRepository.updateSortIndex(it)
                _calculationDragOrder.update { null }
            }
        }
    }

    fun deleteCalculation(id: Long) {
        persistentCalculationList.value.find { it.id == id }?.let { calculation ->
            viewModelScope.launch {
                calculationListRepository.deleteItem(calculation)
            }
        }
    }

    fun handleKeyAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.InsertText -> insertText(action)
            is CalculatorAction.DeleteChars -> removeChars(action.nChars)
            is CalculatorAction.SubmitCalculation -> submitActiveCalculation()
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
        val undoState = undoManager.undo(_activeCalculationInput.value.input)
        if (undoState !== null)
            _activeCalculationInput.update { it.copy(input = undoState, doAutocomplete = false) }
    }

    fun redo() {
        val redoState = undoManager.redo(_activeCalculationInput.value.input)
        if (redoState !== null)
            _activeCalculationInput.update { it.copy(input = redoState, doAutocomplete = false) }
    }

    fun moveCursor(chars: Int) {
        val newCursorPosition =
            (_activeCalculationInput.value.input.selection.end + chars).coerceIn(
                0,
                _activeCalculationInput.value.input.text.length
            )
        _activeCalculationInput.update {
            it.copy(input = it.input.copy(selection = TextRange(newCursorPosition)))
        }
    }

    fun updateInput(input: TextFieldValue, doAutocomplete: Boolean = false) {
        val textChanged = input.text != _activeCalculationInput.value.input.text
        if (textChanged)
            undoManager.snapshot(_activeCalculationInput.value.input)

        _activeCalculationInput.update {
            it.copy(
                input = input,
                doAutocomplete = doAutocomplete && textChanged
            )
        }

        _activeCalculationInput.value.id?.let { id ->
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
        val maxChars = _activeCalculationInput.value.input.text.length
        val textBeforeSelection =
            _activeCalculationInput.value.input.getTextBeforeSelection(maxChars)
        val selectedText = _activeCalculationInput.value.input.getSelectedText()
        val textAfterSelection =
            _activeCalculationInput.value.input.getTextAfterSelection(maxChars)

        with(action) {
            if (selectedText.isNotEmpty()) {
                val newText = when (selectionPolicy) {
                    CalculatorAction.InsertText.SelectionPolicy.REPLACE -> "$textBeforeSelection$preCursorText$postCursorText$textAfterSelection"
                    CalculatorAction.InsertText.SelectionPolicy.SURROUND -> "$textBeforeSelection$preCursorText$selectedText$postCursorText$textAfterSelection"
                    CalculatorAction.InsertText.SelectionPolicy.PARENTHESES -> "$textBeforeSelection($selectedText)$preCursorText$postCursorText$textAfterSelection"
                }

                val newSelection = when (selectionPolicy) {
                    CalculatorAction.InsertText.SelectionPolicy.REPLACE -> TextRange(
                        textBeforeSelection.length + preCursorText.length
                    )

                    CalculatorAction.InsertText.SelectionPolicy.SURROUND -> TextRange(
                        textBeforeSelection.length,
                        newText.length - textAfterSelection.length
                    )

                    CalculatorAction.InsertText.SelectionPolicy.PARENTHESES -> TextRange(newText.length - postCursorText.length - textAfterSelection.length)
                }

                updateInput(
                    TextFieldValue(
                        text = newText,
                        selection = newSelection
                    )
                )
            } else {
                updateInput(
                    TextFieldValue(
                        text = "$textBeforeSelection$preCursorText$postCursorText$textAfterSelection",
                        selection = TextRange(textBeforeSelection.length + preCursorText.length)
                    )
                )
            }
        }
    }

    fun replaceRange(range: TextRange, preCursorText: String, postCursorText: String = "") {
        val newText = _activeCalculationInput.value.input.text.replaceRange(
            range.start,
            range.end,
            "$preCursorText$postCursorText"
        )
        val newCursorPosition = range.start + preCursorText.length

        updateInput(
            TextFieldValue(
                text = newText,
                selection = TextRange(newCursorPosition)
            )
        )
    }

    fun insertText(preCursorText: String, postCursorText: String = "") {
        val maxChars = _activeCalculationInput.value.input.text.length
        val textBeforeSelection =
            _activeCalculationInput.value.input.getTextBeforeSelection(maxChars)
        val textAfterSelection =
            _activeCalculationInput.value.input.getTextAfterSelection(maxChars)
        val newText = "$textBeforeSelection$preCursorText$postCursorText$textAfterSelection"
        val newCursorPosition = textBeforeSelection.length + preCursorText.length

        updateInput(
            TextFieldValue(
                text = newText,
                selection = TextRange(newCursorPosition)
            )
        )
    }

    fun removeChars(nChars: Int) {
        val maxChars = _activeCalculationInput.value.input.text.length
        var textBeforeSelection =
            _activeCalculationInput.value.input.getTextBeforeSelection(maxChars).text
        var textAfterSelection =
            _activeCalculationInput.value.input.getTextAfterSelection(maxChars).text

        if (_activeCalculationInput.value.input.selection.length == 0) {
            if (nChars > 0) {
                textAfterSelection = textAfterSelection.drop(nChars.absoluteValue)
            } else if (nChars < 0) {
                textBeforeSelection = textBeforeSelection.dropLast(nChars.absoluteValue)
            }
        }

        val newText = "$textBeforeSelection$textAfterSelection"
        val newCursorPosition = textBeforeSelection.length

        updateInput(
            TextFieldValue(
                text = newText,
                selection = TextRange(newCursorPosition)
            )
        )
    }

    fun acceptAutocomplete(autocompleteItem: AutocompleteItem) {
        replaceRange(
            activeCalculationData.value.autocompleteResult.contextRange,
            autocompleteItem.typeBeforeCursor,
            autocompleteItem.typeAfterCursor
        )
    }
}