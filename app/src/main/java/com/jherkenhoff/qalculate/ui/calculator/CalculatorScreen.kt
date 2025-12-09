package com.jherkenhoff.qalculate.ui.calculator

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
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
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.Key
import com.jherkenhoff.qalculate.model.KeyAction
import com.jherkenhoff.qalculate.model.KeyLabel
import com.jherkenhoff.qalculate.model.KeyRole
import com.jherkenhoff.qalculate.model.Keys
import com.jherkenhoff.qalculate.model.UserPreferences
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.math.max

private val secondaryKeypadKeys: Array<Array<Key>> = arrayOf(
    arrayOf(Keys.keyX, Keys.keyY, Keys.keyZ, Keys.keySiWeight, Keys.keyFactorial),
    arrayOf(Keys.keyIntegral, Keys.keyDifferential, Keys.keySum, Keys.keyImaginary, Keys.keyComplexOperators),
    arrayOf(Keys.keySin, Keys.keyCos, Keys.keyTan, Keys.keyLn, Keys.keyInfinity)
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
    onKeyAction: (KeyAction) -> Unit = { },
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

    val keyDecimal = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text(decimalChar), decimalChar),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("␣"), " "),
        role = KeyRole.NUMBER
    )

    // TODO: Move dynamic multiplication and division selection to some sort of key-factory
    val multiplicationChar = userPreferences.getMultiplicationSignString()
    val keyMultiply = Key.DefaultKey(clickAction = KeyAction.InsertText.operator(KeyLabel.Text(multiplicationChar), multiplicationChar), role = KeyRole.OPERATOR)

    val divisionChar = userPreferences.getDivisionSignString()
    val keyDivision = Key.DefaultKey(clickAction = KeyAction.InsertText.operator(KeyLabel.Text(divisionChar), divisionChar), role = KeyRole.OPERATOR)

    val primaryKeypadKeys : Array<Array<Key>> = arrayOf(
        arrayOf(Keys.keyPercent, Keys.keyPi, Keys.key7, Keys.key8, Keys.key9, Keys.keyBackspace, Keys.keyClearAll),
        arrayOf(Keys.keySqrt, Keys.keyPower, Keys.key4, Keys.key5, Keys.key6, keyMultiply, keyDivision),
        arrayOf(Keys.keyBracketOpen, Keys.keyBracketClose, Keys.key1, Keys.key2, Keys.key3, Keys.keyPlus, Keys.keyMinus),
        arrayOf(Keys.keyUnderscore, Keys.keyEqual, Keys.key0, keyDecimal, Keys.keyExp, Keys.keyReturn),
    )

    var calculationHistorySize by remember{ mutableIntStateOf(calculationHistory.size)}
    calculationHistorySize = calculationHistory.size

    val historyListState = rememberLazyListState()

    var maxOffset by remember { mutableFloatStateOf(0f) }
    val offsetY = remember { Animatable(0f) }

    val nestedScrollConnectionInputSheet = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
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

                offsetY.animateTo(targetOffset)

                if (targetOffset == 0f) {
                    if (calculationHistorySize != 0) {
                        historyListState.animateScrollToItem(calculationHistorySize)
                    }
                }

                return available
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeContent))
        CalculationHistoryList(
            calculationHistory,
            onDeleteClick = onDeleteCalculation,
            scrollState = historyListState,
            modifier = Modifier.weight(1f).zIndex(1f)
        )

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
                .imeNestedScroll()
                .nestedScroll(nestedScrollConnectionInputSheet)
                .scrollable(
                    rememberScrollState(),
                    orientation = Orientation.Vertical
                )
                .zIndex(2f)
        )

        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background).zIndex(3f),
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
                shadowElevation = 6.dp,
                modifier = Modifier.zIndex(4f)
            ) {
                Column {
                    Column(
                        Modifier.clipToBounds().shrinkHeightAbsolute(offsetY.value.toInt()).onGloballyPositioned{
                            maxOffset = it.size.height.toFloat()
                        }
                    ) {
                        AnimatedVisibility(!keyboardEnabled) {
                            Keypad(
                                secondaryKeypadKeys,
                                onKeyAction = onKeyAction,
                                compact = keyboardEnabled,
                                topKeyCornerSize = CornerSize(21.dp),
                            )
                        }
                        Keypad(
                            primaryKeypadKeys,
                            onKeyAction = onKeyAction,
                            compact = keyboardEnabled,
                            topKeyCornerSize = if (!imeFullyHidden) CornerSize(21.dp) else KeyDefaults.Shape.topStart,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    AuxiliaryBar(
                        autocompleteResult = internalAutocompleteResult,
                        keyboardEnable = keyboardEnabled,
                        onAutocompleteClick = onAutocompleteClick,
                        onKeyboardEnableChange = {keyboardEnabled = it},
                        onKeyAction = onKeyAction,
                        onAutocompleteDismiss = { autocompleteDismissed = true },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).navigationBarsPadding().imePadding()
                    )
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
        calculationHistory = emptyList()
    )
}