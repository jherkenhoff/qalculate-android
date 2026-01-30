package com.jherkenhoff.qalculate.ui.calculator

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Science
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
import com.jherkenhoff.qalculate.model.KeyPositionSpec
import com.jherkenhoff.qalculate.model.KeyRole
import com.jherkenhoff.qalculate.model.KeySpec
import com.jherkenhoff.qalculate.model.KeypadSection
import com.jherkenhoff.qalculate.model.KeypadSpec
import com.jherkenhoff.qalculate.model.Keys
import com.jherkenhoff.qalculate.model.UndoState
import com.jherkenhoff.qalculate.model.UserPreferences
import com.jherkenhoff.qalculate.ui.theme.QalculateTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime

private val secondaryKeypad: List<Pair<KeyPositionSpec, KeySpec>> = listOf(
    Pair(KeyPositionSpec(0, 0), Keys.keySpecX),
    Pair(KeyPositionSpec(0, 1), Keys.keySpecY),
    Pair(KeyPositionSpec(0, 2), Keys.keySpecZ),
    Pair(KeyPositionSpec(0, 3), Keys.keySpecSiPrefix),
    Pair(KeyPositionSpec(0, 4), Keys.keySpecBasicUnits),

    Pair(KeyPositionSpec(1, 0), Keys.keySpecIntegral),
    Pair(KeyPositionSpec(1, 1), Keys.keySpecDifferential),
    Pair(KeyPositionSpec(1, 2), Keys.keySpecSum),
    Pair(KeyPositionSpec(1, 3), Keys.keySpecImaginary),
    Pair(KeyPositionSpec(1, 4), Keys.keySpecComplexOperators),

    Pair(KeyPositionSpec(2, 0), Keys.keySpecSin),
    Pair(KeyPositionSpec(2, 1), Keys.keySpecCos),
    Pair(KeyPositionSpec(2, 2), Keys.keySpecTan),
    Pair(KeyPositionSpec(2, 3), Keys.keySpecLn),
    Pair(KeyPositionSpec(2, 4), Keys.keySpecInfinity)
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
        onInputFieldValueChange = { viewModel.updateInput(it, true) },
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

    val imeHeight = WindowInsets.ime.getBottom(localDensity)
    val imeFullyHidden = imeHeight == 0

    var autocompleteDismissed by remember { mutableStateOf(false) }
    if (autocompleteResult.relevantText.isEmpty()) {
        autocompleteDismissed = false
    }

    val internalAutocompleteResult = if (autocompleteDismissed) AutocompleteResult() else autocompleteResult

    // TODO: Move dynamic decimal selection to some sort of key-factory
    val decimalChar = when (userPreferences.decimalSeparator) {
        UserPreferences.DecimalSeparator.DOT -> "."
        UserPreferences.DecimalSeparator.COMMA -> ","
    }
    val otherChar = when (userPreferences.decimalSeparator) {
        UserPreferences.DecimalSeparator.DOT -> ","
        UserPreferences.DecimalSeparator.COMMA -> "."
    }

    val keySpecDecimal = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text(decimalChar), decimalChar),
        longClickAction = Action.InsertText(ActionLabel.Text("␣"), " "),
        role = KeyRole.NUMBER
    )

    // TODO: Move dynamic multiplication and division selection to some sort of key-factory
    val multiplicationChar = userPreferences.getMultiplicationSignString()
    val keySpecMultiply = KeySpec.DefaultKeySpec(clickAction = Action.InsertText.operator(ActionLabel.Text(multiplicationChar), multiplicationChar), role = KeyRole.OPERATOR)

    val divisionChar = userPreferences.getDivisionSignString()
    val keySpecDivision = KeySpec.DefaultKeySpec(clickAction = Action.InsertText.operator(ActionLabel.Text(divisionChar), divisionChar), role = KeyRole.OPERATOR)

    val primaryKeypad : List<Pair<KeyPositionSpec, KeySpec>> = listOf(
        Pair(KeyPositionSpec(0, 0), Keys.keySpecPercent),
        Pair(KeyPositionSpec(0, 1), Keys.keySpecPi),
        Pair(KeyPositionSpec(0, 2), Keys.keySpec7),
        Pair(KeyPositionSpec(0, 3), Keys.keySpec8),
        Pair(KeyPositionSpec(0, 4), Keys.keySpec9),
        Pair(KeyPositionSpec(0, 5), Keys.keySpecBackspace),
        Pair(KeyPositionSpec(0, 6), Keys.keySpecClearAll),

        Pair(KeyPositionSpec(1, 0), Keys.keySpecSqrt),
        Pair(KeyPositionSpec(1, 1), Keys.keySpecPower),
        Pair(KeyPositionSpec(1, 2), Keys.keySpec4),
        Pair(KeyPositionSpec(1, 3), Keys.keySpec5),
        Pair(KeyPositionSpec(1, 4), Keys.keySpec6),
        Pair(KeyPositionSpec(1, 5), keySpecMultiply),
        Pair(KeyPositionSpec(1, 6), keySpecDivision),

        Pair(KeyPositionSpec(2, 0), Keys.keySpecBracketOpen),
        Pair(KeyPositionSpec(2, 1), Keys.keySpecBracketClose),
        Pair(KeyPositionSpec(2, 2), Keys.keySpec1),
        Pair(KeyPositionSpec(2, 3), Keys.keySpec2),
        Pair(KeyPositionSpec(2, 4), Keys.keySpec3),
        Pair(KeyPositionSpec(2, 5), Keys.keySpecPlus),
        Pair(KeyPositionSpec(2, 6), Keys.keySpecMinus),

        Pair(KeyPositionSpec(3, 0), Keys.keySpecUnderscore),
        Pair(KeyPositionSpec(3, 1), Keys.keySpecEqual),
        Pair(KeyPositionSpec(3, 2), Keys.keySpec0),
        Pair(KeyPositionSpec(3, 3), keySpecDecimal),
        Pair(KeyPositionSpec(3, 4), Keys.keySpecExp),
        Pair(KeyPositionSpec(3, 5, 1, 2), Keys.keySpecReturn),
    )

    val keypads = listOf(
        KeypadSpec(
            name = "Keyboard",
            icon = Icons.Default.Keyboard,
            sections = listOf(
                KeypadSection(4, 7, primaryKeypad, 0.6f)
            ),
            imeEnabled = true
        ),
        KeypadSpec(
            name = "Advanced",
            icon = Icons.Default.Science,
            sections = listOf(
                KeypadSection(3, 5, secondaryKeypad, 0.5f),
                KeypadSection(4, 7, primaryKeypad, 0.9f)
            ),
            imeEnabled = false
        )
    )

    var activeKeypad by remember { mutableIntStateOf(0) }

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

                if (calculationHistory.isNotEmpty() && targetOffset == 0f) {
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
                    interceptKeyboard = !keypads[activeKeypad].imeEnabled,
                    modifier = Modifier
                        .nestedScroll(nestedScrollConnectionInputSheet)
                        .scrollable(
                            rememberScrollState(),
                            orientation = Orientation.Vertical
                        )
                )

                Keypad(
                    keypads,
                    activeKeypad,
                    Modifier.clipToBounds()
                        .shrinkHeightAbsolute(offsetY.value.toInt())
                        .onGloballyPositioned { maxOffset = it.size.height.toFloat() }
                )

            }
        }
    }
}


@Preview(name = "Light Mode", showSystemUi = true, device = Devices.DEFAULT)
@Preview(name = "Dark Mode", showSystemUi = true, device = Devices.DEFAULT, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DefaultPreview() {
    QalculateTheme() {
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
                )
            )
        )
    }
}


@Preview(showSystemUi = true, device = Devices.DEFAULT)
@Composable
private fun ManyHistoryItemsPreview() {

    QalculateTheme() {
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
}

@Preview(showSystemUi = true, device = Devices.DEFAULT)
@Composable
private fun EmptyHistoryPreview() {
    QalculateTheme() {
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
}