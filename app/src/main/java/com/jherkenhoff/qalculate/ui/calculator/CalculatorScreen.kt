package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jherkenhoff.qalculate.domain.AutocompleteResult
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.Calculation
import com.jherkenhoff.qalculate.model.Key
import com.jherkenhoff.qalculate.model.KeyAction
import com.jherkenhoff.qalculate.model.Keys
import kotlinx.coroutines.launch
import java.util.UUID


private data class SecondaryKeypadData(
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
    // val autocompleteResult by viewModel.autocompleteResult.collectAsStateWithLifecycle()

    CalculatorScreenContent(
        inputTextFieldValue = viewModel.inputTextFieldValue.collectAsStateWithLifecycle().value,
        parsedString = viewModel.parsedString.collectAsStateWithLifecycle().value,
        resultString = viewModel.resultString.collectAsStateWithLifecycle().value,
        onKeyAction = viewModel::handleKeyAction,
        calculationHistory = viewModel.calculations.collectAsState().value,
        autocompleteResult = viewModel.autocompleteResult.collectAsStateWithLifecycle().value,
        onInputFieldValueChange = viewModel::updateInput,
        onDeleteCalculation = viewModel::deleteCalculation,
        onCalculationSubmit = viewModel::submitCalculation,
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
    calculationHistory: Map<UUID, Calculation> = emptyMap(),
    autocompleteResult: AutocompleteResult,
    onKeyAction: (KeyAction) -> Unit = { },
    onInputFieldValueChange: (TextFieldValue) -> Unit = { },
    onDeleteCalculation: (UUID) -> Unit = { },
    onCalculationSubmit: () -> Unit = { },
    onAutocompleteClick: (AutocompleteItem) -> Unit = { },
    onMenuClick: () -> Unit = {  },
    onSettingsClick: () -> Unit = {  }
) {
    var keyboardInputEnabled by remember { mutableStateOf(true) }

    //val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    var lastImeHeight by remember { mutableIntStateOf(0) }

    val isImeVisible = (imeHeight != 0) && (imeHeight >= lastImeHeight)
    lastImeHeight = imeHeight

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    fun deleteCalculationWithSnackbar(uuid: UUID) {
        scope.launch {
            snackbarHostState.showSnackbar(
                "Deleted calculation " + calculationHistory[uuid]?.input?.text,
                actionLabel = "Undo",
                withDismissAction = true,
                duration = SnackbarDuration.Short
            )
        }

        onDeleteCalculation(uuid)
    }

    var autocompleteDismissed by remember { mutableStateOf(false) }

    if (autocompleteResult.relevantText.isEmpty()) {
        autocompleteDismissed = false
    }

    val internalAutocompleteResult = if (autocompleteDismissed) AutocompleteResult() else autocompleteResult

    var activeSecondaryKeypad by remember { mutableIntStateOf(0) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            CalculatorTopBar(
                onMenuClick = onMenuClick,
                onSettingsClick = onSettingsClick
            )

            Spacer(Modifier.weight(1f))

            InputSheet(
                inputTextFieldValue,
                parsedString,
                resultString,
                internalAutocompleteResult,
                onInputFieldValueChange,
                {},
                interceptKeyboard = !keyboardInputEnabled,
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp)
                    )
            ) {

                AnimatedVisibility(!isImeVisible) {
                    Column {
                        PrimaryTabRow(
                            activeSecondaryKeypad,
                            containerColor = Color.Transparent,
                            divider = { Spacer(Modifier.height(4.dp)) }
                        ) {
                            for ((i, keypad) in secondaryKeypads.withIndex()) {
                                Tab(
                                    selected = true,
                                    onClick = { activeSecondaryKeypad = i},
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
                                compact = isImeVisible
                            )
                        }
                    }
                }
                Keypad(
                    primaryKeypadKeys,
                    onKeyAction = onKeyAction,
                    compact = isImeVisible,
                    topKeyCornerSize = if (isImeVisible) CornerSize(21.dp) else KeyDefaults.Shape.topStart
                )

                AuxiliaryBar(
                    autocompleteResult = internalAutocompleteResult,
                    keyboardEnable = keyboardInputEnabled,
                    onAutocompleteClick = onAutocompleteClick,
                    onKeyboardEnableChange = {keyboardInputEnabled = it},
                    onKeyAction = onKeyAction,
                    onAutocompleteDismiss = { autocompleteDismissed = true }
                )

                Spacer(Modifier.height(8.dp))

                Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeContent))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    CalculatorScreenContent(
        TextFieldValue("c"),
        "SpeedOfLight",
        "299.792 458 Km/ms",
        autocompleteResult = AutocompleteResult()
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
    CalculatorScreenContent(
        TextFieldValue(""),
        "",
        "",
        autocompleteResult = AutocompleteResult()
    )
}