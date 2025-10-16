package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastRoundToInt
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
import kotlin.math.roundToInt


data class SecondaryKeypadData(
    val title: String,
    val keys: Array<Array<Key>>
)

private val basicSecondaryKeypad = SecondaryKeypadData(
    title = "Basic",
    keys = arrayOf(
        arrayOf(Keys.keySiLength, Keys.keyImperialLength, Keys.keyImperialWeight, Keys.keySiWeight, Keys.keyConversion),
        arrayOf(Keys.keyIntegral, Keys.keyDifferential, Keys.keySum, Keys.keyX, Keys.keyInfinity),
        arrayOf(Keys.keySin, Keys.keyCos, Keys.keyTan, Keys.keyLn, Keys.keyImaginary)
    )
)

private val physicsSecondaryKeypad = SecondaryKeypadData(
    title = "Physics",
    keys = arrayOf(
        arrayOf(Keys.keySiLength, Keys.keyImperialLength, Keys.keyImperialWeight, Keys.keySiWeight, Keys.keyConversion),
    )
)

private val secondaryKeypads = arrayOf(basicSecondaryKeypad, physicsSecondaryKeypad)



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
    var keyboardEnabled by remember { mutableStateOf(false) }

    //val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    var lastImeHeight by remember { mutableIntStateOf(0) }

    val isImeVisible = true //(imeHeight != 0) && (imeHeight >= lastImeHeight)

    val isImeFullyClosed = imeHeight == 0

//    LaunchedEffect(isImeFullyClosed) {
//        if (isImeFullyClosed) {
//            keyboardEnabled = false
//        }
//    }

    @Suppress("AssignedValueIsNeverRead")
    lastImeHeight = imeHeight

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

//    fun deleteCalculationWithSnackbar(uuid: UUID) {
//        scope.launch {
//            snackbarHostState.showSnackbar(
//                "Deleted calculation " + calculationHistoryItemHistory[uuid]?.input,
//                actionLabel = "Undo",
//                withDismissAction = true,
//                duration = SnackbarDuration.Short
//            )
//        }
//
//        onDeleteCalculation(uuid)
//    }

    var autocompleteDismissed by remember { mutableStateOf(false) }

    if (autocompleteResult.relevantText.isEmpty()) {
        autocompleteDismissed = false
    }

    val internalAutocompleteResult = if (autocompleteDismissed) AutocompleteResult() else autocompleteResult

    var activeSecondaryKeypad by remember { mutableIntStateOf(0) }

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
        bottomRightAction = KeyAction.InsertText(KeyLabel.Text(otherChar), otherChar),
        bottomLeftAction = KeyAction.InsertText(KeyLabel.Text(";"), ";"),
        role = KeyRole.NUMBER
    )

    val multiplicationChar = userPreferences.getMultiplicationSignString()
    val keyMultiply = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text(multiplicationChar), multiplicationChar), role = KeyRole.OPERATOR)

    val divisionChar = userPreferences.getDivisionSignString()
    val keyDivision = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text(divisionChar), divisionChar), role = KeyRole.OPERATOR)

    val primaryKeypadKeys : Array<Array<Key>> = arrayOf(
        arrayOf(Keys.keyPercent, Keys.keyPi, Keys.key7, Keys.key8, Keys.key9, Keys.keyBackspace, Keys.keyClearAll),
        arrayOf(Keys.keySqrt, Keys.keyPower, Keys.key4, Keys.key5, Keys.key6, keyMultiply, keyDivision),
        arrayOf(Keys.keyBracketOpen, Keys.keyBracketClose, Keys.key1, Keys.key2, Keys.key3, Keys.keyPlus, Keys.keyMinus),
        arrayOf(Keys.keyUnderscore, Keys.keyEqual, Keys.key0, keyDecimal, Keys.keyExp, Keys.keyReturn),
    )

    val historyListState = rememberLazyListState()

    var secondaryKeypadVisible = true //!isImeVisible

    var maxOffset by remember { mutableFloatStateOf(0f) }
    val offsetY = remember { Animatable(0f) }

    // Custom nested scroll connection
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                scope.launch {
                    val newOffset = (offsetY.value + available.y).coerceIn(0f, maxOffset)
                    offsetY.snapTo(newOffset)
                }
                return Offset(0f, available.y) // Consume the scroll
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                scope.launch {
                    val newOffset = (offsetY.value + available.y).coerceIn(0f, maxOffset)
                    offsetY.snapTo(newOffset)
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                available.y
                // Handle fling gesture: decide whether to open or close
                if (offsetY.value > 100f) {
                    offsetY.animateTo(maxOffset)
                } else {
                    offsetY.animateTo(0f)
                }
                return Velocity.Zero
            }
        }
    }


    Surface(
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            CalculatorTopBar(
                userPreferences = userPreferences,
                onUserPreferencesChanged = onUserPreferencesChanged,
                onMenuClick = onMenuClick,
                onSettingsClick = onSettingsClick
            )

            SubcomposeLayout(
                Modifier
                    .clipToBounds()
                    .scrollable(rememberScrollState(), Orientation.Vertical)
                    .weight(1f)
            ) { constraints ->
                var remainingHeight = constraints.maxHeight

                val bottomBarPlaceable = subcompose("bottom_bar") {
                    Box(
                        modifier = Modifier.fillMaxWidth().background(
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp)
                        )
                    ){
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
                }[0].measure(constraints.copyMaxDimensions())


                remainingHeight = remainingHeight - bottomBarPlaceable.height

                val keypadPlaceable = subcompose("keypad") {
                    Column {
                        AnimatedVisibility(secondaryKeypadVisible) {
                            Column {
                                PrimaryTabRow(
                                    activeSecondaryKeypad,
                                    containerColor = Color.Transparent,
                                    divider = { Spacer(Modifier.height(4.dp)) }
                                ) {
                                    for ((i, keypad) in secondaryKeypads.withIndex()) {
                                        Tab(
                                            selected = true,
                                            onClick = { activeSecondaryKeypad = i },
                                            text = {
                                                Text(keypad.title)
                                            }
                                        )
                                    }
                                }

                                AnimatedContent(activeSecondaryKeypad) {
                                    Keypad(
                                        secondaryKeypads[it].keys,
                                        onKeyAction = onKeyAction,
                                        compact = !secondaryKeypadVisible
                                    )
                                }
                            }
                        }
                        Keypad(
                            primaryKeypadKeys,
                            onKeyAction = onKeyAction,
                            compact = !secondaryKeypadVisible,
                            topKeyCornerSize = if (!secondaryKeypadVisible) CornerSize(21.dp) else KeyDefaults.Shape.topStart,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }[0].measure(constraints.copyMaxDimensions().copy(maxHeight = remainingHeight))

                remainingHeight = remainingHeight - keypadPlaceable.height

                val bottomSheetBackgroundPlaceable = subcompose("bottom_sheet_background") {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
                    ) {

                    }
                }[0].measure(constraints.copyMaxDimensions().copy(maxHeight = bottomBarPlaceable.height + keypadPlaceable.height))

                maxOffset = keypadPlaceable.height.toFloat()

                val inputSectionPlaceable = subcompose("input") {
                    InputSheet(
                        inputTextFieldValue,
                        parsedString,
                        resultString,
                        internalAutocompleteResult,
                        onInputFieldValueChange,
                        {},
                        interceptKeyboard = false, //!keyboardEnabled
                    )
                }[0].measure(constraints.copyMaxDimensions())

                val historyHeight = constraints.maxHeight - keypadPlaceable.height - inputSectionPlaceable.height - bottomBarPlaceable.height + offsetY.value.fastRoundToInt()

                val historyPlaceable = subcompose("history") {
                    CalculationHistoryList(
                        calculationHistory,
                        onDeleteClick = onDeleteCalculation,
                        scrollState = historyListState,
                        modifier = Modifier.nestedScroll(nestedScrollConnection)
                    )
                }[0].measure(constraints.copy(minHeight = historyHeight, maxHeight = historyHeight))

                layout(constraints.maxWidth, constraints.maxHeight) {
                    inputSectionPlaceable.place(0, constraints.maxHeight - keypadPlaceable.height - bottomBarPlaceable.height - inputSectionPlaceable.height + offsetY.value.roundToInt())
                    bottomSheetBackgroundPlaceable.place(0, (constraints.maxHeight - bottomSheetBackgroundPlaceable.height + offsetY.value.roundToInt()))
                    keypadPlaceable.place(0, constraints.maxHeight - keypadPlaceable.height - bottomBarPlaceable.height + offsetY.value.roundToInt())
                    historyPlaceable.place(0, 0)
                    bottomBarPlaceable.place(0, constraints.maxHeight - bottomBarPlaceable.height)
                }
            }
        }
    }
}


@Preview(showSystemUi = true, device = Devices.PIXEL_9_PRO)
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


@Preview(showSystemUi = true, device = Devices.PIXEL_9_PRO)
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

@Preview(showSystemUi = true, device = Devices.PIXEL_9_PRO)
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