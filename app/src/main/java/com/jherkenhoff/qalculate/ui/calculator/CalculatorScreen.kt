package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import com.jherkenhoff.qalculate.domain.AutocompleteResult
import com.jherkenhoff.qalculate.model.Action
import com.jherkenhoff.qalculate.model.ActionLabel
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.KeyRole
import com.jherkenhoff.qalculate.model.KeySpec
import com.jherkenhoff.qalculate.model.Keys
import com.jherkenhoff.qalculate.model.PositionedKeySpec
import com.jherkenhoff.qalculate.model.UndoState
import com.jherkenhoff.qalculate.model.UserPreferences
import kotlinx.coroutines.launch
import java.time.LocalDateTime

private val secondaryKeypad: List<PositionedKeySpec> = listOf(
    PositionedKeySpec(0, 0, Keys.keySpecX),
    PositionedKeySpec(0, 1, Keys.keySpecY),
    PositionedKeySpec(0, 2, Keys.keySpecZ),
    PositionedKeySpec(0, 3, Keys.keySpecSiWeight),
    PositionedKeySpec(0, 4, Keys.keySpecFactorial),

    PositionedKeySpec(1, 0, Keys.keySpecIntegral),
    PositionedKeySpec(1, 1, Keys.keySpecDifferential),
    PositionedKeySpec(1, 2, Keys.keySpecSum),
    PositionedKeySpec(1, 3, Keys.keySpecImaginary),
    PositionedKeySpec(1, 4, Keys.keySpecComplexOperators),

    PositionedKeySpec(2, 0, Keys.keySpecSin),
    PositionedKeySpec(2, 1, Keys.keySpecCos),
    PositionedKeySpec(2, 2, Keys.keySpecTan),
    PositionedKeySpec(2, 3, Keys.keySpecLn),
    PositionedKeySpec(2, 4, Keys.keySpecInfinity)
)

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel(),
    openDrawer: () -> Unit = { },
    openSettings: () -> Unit = { }
) {
    CalculatorScreenContent(
        inputTextFieldValue = viewModel.inputTextFieldValue.collectAsStateWithLifecycle().value,
        parsedString = viewModel.parsedString.collectAsStateWithLifecycle().value,
        resultString = viewModel.resultString.collectAsStateWithLifecycle().value,
        userPreferences = viewModel.userPreferences.collectAsStateWithLifecycle().value,
        onUserPreferencesChanged = viewModel::updateUserPreferences,
        calculationHistory = viewModel.calculationHistory.collectAsStateWithLifecycle().value,
        onKeyAction = viewModel::handleKeyAction,
        autocompleteResult = viewModel.autocompleteResult.collectAsStateWithLifecycle().value,
        undoState = viewModel.undoState.collectAsStateWithLifecycle().value,
        onInputFieldValueChange = viewModel::updateInput,
        onDeleteCalculation = viewModel::deleteCalculation,
        onMenuClick = openDrawer,
        onSettingsClick = openSettings,
        onAutocompleteClick = viewModel::acceptAutocomplete
    )
}

fun Modifier.shrinkHeightAbsolute(shrinkPx: Int): Modifier = this.then(
    Modifier.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val height = (placeable.height - shrinkPx).coerceAtLeast(0)
        layout(placeable.width, height) {
            // Place at top; clip the bottom part
            placeable.place(0, 0)
        }
    }
)


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalSharedTransitionApi::class, ExperimentalLayoutApi::class
)
@Composable
fun CalculatorScreenContent(
    inputTextFieldValue: TextFieldValue,
    parsedString: String,
    resultString: String,
    userPreferences: UserPreferences,
    onUserPreferencesChanged : (UserPreferences) -> Unit,
    calculationHistory: List<CalculationHistoryItemData> = emptyList(),
    autocompleteResult: AutocompleteResult,
    undoState: UndoState<TextFieldValue>,
    onKeyAction: (Action) -> Unit = { },
    onInputFieldValueChange: (TextFieldValue) -> Unit = { },
    onDeleteCalculation: (CalculationHistoryItemData) -> Unit = { },
    onAutocompleteClick: (AutocompleteItem) -> Unit = { },
    onMenuClick: () -> Unit = {  },
    onSettingsClick: () -> Unit = {  },
) {
    val scope = rememberCoroutineScope()
    val localDensity = LocalDensity.current

    var keyboardEnabled by remember { mutableStateOf(false) }

    val imeHeight = WindowInsets.ime.getBottom(localDensity)
    val imeFullyHidden = imeHeight == 0

    var autocompleteDismissed by remember { mutableStateOf(false) }
    if (autocompleteResult.relevantText.isEmpty()) {
        autocompleteDismissed = false
    }

    val internalAutocompleteResult = if (autocompleteDismissed) AutocompleteResult() else autocompleteResult

    var activeSecondaryKeypad by remember { mutableIntStateOf(0) }

    // TODO: Move dynamic decimal selection to some sort of key-factory
    val decimalChar = when (userPreferences.decimalSeparator) {
        UserPreferences.DecimalSeparator.DOT -> "."
        UserPreferences.DecimalSeparator.COMMA -> ","
    }
    val otherChar = when (userPreferences.decimalSeparator) {
        UserPreferences.DecimalSeparator.DOT -> ","
        UserPreferences.DecimalSeparator.COMMA -> "."
    }

    val keySpecDecimal = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text(decimalChar), decimalChar),
        topRightAction = Action.InsertText(ActionLabel.Text("␣"), " "),
        role = KeyRole.NUMBER
    )

    // TODO: Move dynamic multiplication and division selection to some sort of key-factory
    val multiplicationChar = userPreferences.getMultiplicationSignString()
    val keySpecMultiply = KeySpec.DefaultKeySpec(clickAction = Action.InsertText.operator(ActionLabel.Text(multiplicationChar), multiplicationChar), role = KeyRole.OPERATOR)

    val divisionChar = userPreferences.getDivisionSignString()
    val keySpecDivision = KeySpec.DefaultKeySpec(clickAction = Action.InsertText.operator(ActionLabel.Text(divisionChar), divisionChar), role = KeyRole.OPERATOR)

    val primaryKeypad : List<PositionedKeySpec> = listOf(
        PositionedKeySpec(0, 0, Keys.keySpecPercent),
        PositionedKeySpec(0, 1, Keys.keySpecPi),
        PositionedKeySpec(0, 2, Keys.keySpec7),
        PositionedKeySpec(0, 3, Keys.keySpec8),
        PositionedKeySpec(0, 4, Keys.keySpec9),
        PositionedKeySpec(0, 5, Keys.keySpecBackspace),
        PositionedKeySpec(0, 6, Keys.keySpecClearAll),

        PositionedKeySpec(1, 0, Keys.keySpecSqrt),
        PositionedKeySpec(1, 1, Keys.keySpecPower),
        PositionedKeySpec(1, 2, Keys.keySpec4),
        PositionedKeySpec(1, 3, Keys.keySpec5),
        PositionedKeySpec(1, 4, Keys.keySpec6),
        PositionedKeySpec(1, 5, keySpecMultiply),
        PositionedKeySpec(1, 6, keySpecDivision),

        PositionedKeySpec(2, 0, Keys.keySpecBracketOpen),
        PositionedKeySpec(2, 1, Keys.keySpecBracketClose),
        PositionedKeySpec(2, 2, Keys.keySpec1),
        PositionedKeySpec(2, 3, Keys.keySpec2),
        PositionedKeySpec(2, 4, Keys.keySpec3),
        PositionedKeySpec(2, 5, Keys.keySpecPlus),
        PositionedKeySpec(2, 6, Keys.keySpecMinus),

        PositionedKeySpec(3, 0, Keys.keySpecUnderscore),
        PositionedKeySpec(3, 1, Keys.keySpecEqual),
        PositionedKeySpec(3, 2, Keys.keySpec0),
        PositionedKeySpec(3, 3, keySpecDecimal),
        PositionedKeySpec(3, 4, Keys.keySpecExp),
        PositionedKeySpec(3, 5, 1, 2, Keys.keySpecReturn),
    )

    val historyListState = rememberLazyListState()

    var maxOffset by remember { mutableFloatStateOf(0f) }
    val offsetY = remember { Animatable(0f) }

    val nestedScrollConnectionInputSheet = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y <= 0) {
                    return Offset.Zero
                }
                val newOffset = (offsetY.value + available.y)
                val clippedNewOffset = newOffset.coerceIn(0f, maxOffset)
                scope.launch {
                    offsetY.snapTo(clippedNewOffset)
                }
                return Offset(0f, available.y - (newOffset - clippedNewOffset))
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                if (available.y >= 0) {
                    return Offset.Zero
                }

                val newOffset = (offsetY.value + available.y)
                val clippedNewOffset = newOffset.coerceIn(0f, maxOffset)
                scope.launch {
                    offsetY.snapTo(clippedNewOffset)
                }
                return Offset(0f, available.y - (newOffset - clippedNewOffset))
            }

            override suspend fun onPreFling(available: Velocity): Velocity {

                val velocityThreshold = with(localDensity) { 1000.dp.toPx() }

                val targetOffset = when {
                    available.y > velocityThreshold -> maxOffset
                    available.y < -velocityThreshold -> 0f
                    else -> {
                        // Not enough momentum. Decide based on position
                        if (offsetY.value > maxOffset / 2) maxOffset else 0f
                    }
                }

                if (targetOffset == 0f) {
                    historyListState.animateScrollToItem(0)
                }

                offsetY.animateTo(targetOffset)

                return available
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeContent))
        CalculationHistoryList(
            calculationHistory,
            onDeleteClick = onDeleteCalculation,
            scrollState = historyListState,
            modifier = Modifier.weight(1f)
                .nestedScroll(nestedScrollConnectionInputSheet)
        )

        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
            shadowElevation = 10.dp,
            modifier = Modifier.zIndex(2f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InputSheet(
                    inputTextFieldValue,
                    parsedString,
                    resultString,
                    internalAutocompleteResult,
                    userPreferences,
                    onValueChange = onInputFieldValueChange,
                    onUserPreferencesChanged = onUserPreferencesChanged,
                    onMenuClick = onMenuClick,
                    interceptKeyboard = !keyboardEnabled,
                    modifier = Modifier
                        .nestedScroll(nestedScrollConnectionInputSheet)
                        .scrollable(
                            rememberScrollState(),
                            orientation = Orientation.Vertical
                        )
                )

                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerHighest,
                    shadowElevation = 10.dp,
                    modifier = Modifier.zIndex(3f)
                ) {
                    Column() {

                        val auxiliaryActions = listOf(
                            Action.MoveCursor(
                                ActionLabel.Icon(Icons.Default.ChevronLeft, "Move cursor to the left"),
                                -1,
                                enabled = (inputTextFieldValue.selection.end != 0)
                            ),
                            Action.MoveCursor(
                                ActionLabel.Icon(Icons.Default.ChevronRight, "Move cursor to the right"),
                                1,
                                enabled = (inputTextFieldValue.selection.end != inputTextFieldValue.text.length)
                            ),
                            Action.Undo(
                                ActionLabel.Icon(Icons.AutoMirrored.Filled.Undo, "Undo"),
                                enabled = undoState.canUndo
                            ),
                            Action.Redo(
                                ActionLabel.Icon(Icons.AutoMirrored.Filled.Redo, "Redo"),
                                enabled = undoState.canRedo
                            ),
                        )

                        AuxiliaryBar(
                            autocompleteResult = internalAutocompleteResult,
                            keyboardEnable = keyboardEnabled,
                            auxiliaryActions = auxiliaryActions,
                            onAutocompleteClick = onAutocompleteClick,
                            onKeyboardEnableChange = { keyboardEnabled = it },
                            onAction = onKeyAction,
                            onAutocompleteDismiss = { autocompleteDismissed = true },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Column(
                            Modifier
                                .clipToBounds()
                                .shrinkHeightAbsolute(offsetY.value.toInt())
                                .onGloballyPositioned {
                                    maxOffset = it.size.height.toFloat()
                                }
                                .padding(horizontal = 3.dp)
                        ) {
                            AnimatedVisibility(!keyboardEnabled) {
                                GridLayout(
                                    3,
                                    5,
                                    horizontalSpacing = 3.dp,
                                    verticalSpacing = 3.dp,
                                    aspectRatio = 0.5f,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    secondaryKeypad.forEach {
                                        item(it.row, it.col, it.rowSpan, it.colSpan) {
                                            Key(
                                                it.keySpec,
                                                onKeyAction = onKeyAction
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(3.dp))
                            GridLayout(
                                4,
                                7,
                                horizontalSpacing = 3.dp,
                                verticalSpacing = 3.dp,
                                aspectRatio = if (imeFullyHidden) 0.9f else 0.6f,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                primaryKeypad.forEach {
                                    item(it.row, it.col, it.rowSpan, it.colSpan) {
                                        Key(
                                            it.keySpec,
                                            onKeyAction = onKeyAction
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(WindowInsets.safeContent.getBottom(LocalDensity.current).toDp()))

                    }
                }

            }
        }
    }
}


@Preview(showSystemUi = true, device = Devices.DEFAULT)
@Composable
private fun DefaultPreview() {

    CalculatorScreenContent(
        TextFieldValue("c"),
        "SpeedOfLight",
        "299.792 458 Km/ms",
        userPreferences = UserPreferences(),
        onUserPreferencesChanged = {},
        autocompleteResult = AutocompleteResult(),
        undoState = UndoState<TextFieldValue>(),
        calculationHistory = listOf(
            CalculationHistoryItemData(
                0, "1+1", "1+1", "2", LocalDateTime.now()
            ),
//            CalculationHistoryItemData(
//                1, "2+2", "2+2", "4", LocalDateTime.now()
//            ),
//            CalculationHistoryItemData(
//                2, "2+2", "2+2", "4", LocalDateTime.now()
//            ),
//            CalculationHistoryItemData(
//                3, "2+2", "2+2", "4", LocalDateTime.now().minusDays(1)
//            )
        )
    )
}


@Preview(showSystemUi = true, device = Devices.DEFAULT)
@Composable
private fun ManyHistoryItemsPreview() {

    CalculatorScreenContent(
        TextFieldValue("c"),
        "SpeedOfLight",
        "299.792 458 Km/ms",
        userPreferences = UserPreferences(),
        onUserPreferencesChanged = {},
        autocompleteResult = AutocompleteResult(),
        undoState = UndoState<TextFieldValue>(),
        calculationHistory = listOf(
            CalculationHistoryItemData(
                0, "1+1", "1+1", "2", LocalDateTime.now()
            ),
            CalculationHistoryItemData(
                1, "2+2", "2+2", "4", LocalDateTime.now()
            ),
            CalculationHistoryItemData(
                2, "2+2", "2+2", "4", LocalDateTime.now().minusDays(1)
            ),
            CalculationHistoryItemData(
                3, "2+2", "2+2", "4", LocalDateTime.now().minusDays(1)
            ),
            CalculationHistoryItemData(
                4, "2+2", "2+2", "4", LocalDateTime.now().minusDays(2)
            ),
            CalculationHistoryItemData(
                5, "2+2", "2+2", "4", LocalDateTime.now().minusDays(6)
            ),
            CalculationHistoryItemData(
                6, "2+2", "2+2", "4", LocalDateTime.now().minusDays(6)
            ),
            CalculationHistoryItemData(
                7, "2+2", "2+2", "4", LocalDateTime.now().minusDays(6)
            ),
            CalculationHistoryItemData(
                8, "2+2", "2+2", "4", LocalDateTime.now().minusDays(6)
            ),
            CalculationHistoryItemData(
                9, "2+2", "2+2", "4", LocalDateTime.now().minusDays(6)
            )
        )
    )
}

@Preview(showSystemUi = true, device = Devices.DEFAULT)
@Composable
private fun EmptyHistoryPreview() {
    CalculatorScreenContent(
        TextFieldValue("c"),
        "SpeedOfLight",
        "299.792 458 Km/ms",
        userPreferences = UserPreferences(),
        onUserPreferencesChanged = {},
        autocompleteResult = AutocompleteResult(),
        undoState = UndoState<TextFieldValue>(),
        calculationHistory = emptyList()
    )
}