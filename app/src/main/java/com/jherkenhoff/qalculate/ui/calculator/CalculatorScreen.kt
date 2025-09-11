package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import com.jherkenhoff.qalculate.model.KeyAction
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.max

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
    autocompleteResult: AutocompleteResult? = null,
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
                onInputFieldValueChange,
                {},
                interceptKeyboard = !keyboardInputEnabled,
            )


//            Box(
//                contentAlignment = Alignment.BottomCenter,
//                modifier = Modifier.weight(1f)
//            ) {
//                CalculationList(
//                    calculations,
//                    focusedCalculationUuid,
//                    autocompleteResult = autocompleteResult,
//                    onInputFieldValueChange = onInputFieldValueChange,
//                    onDeleteClick = { deleteCalculationWithSnackbar(it) },
//                    onSubmit = onCalculationSubmit,
//                    onCalculationFocusChange = onCalculationFocusChange,
//                    onAutocompleteClick = onAutocompleteClick,
//                    modifier = Modifier.fillMaxHeight()
//                )
//                SnackbarHost(snackbarHostState)
//            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AnimatedVisibility(!isImeVisible) {
                        SecondaryKeypad(onKeyAction = onKeyAction)
                    }
                    PrimaryKeypad(onKeyAction = onKeyAction)

                    AuxiliaryBar(
                        autocompleteResult = autocompleteResult,
                        keyboardEnable = keyboardInputEnabled,
                        onAutocompleteClick = onAutocompleteClick,
                        onKeyboardEnableChange = {keyboardInputEnabled = it},
                        onKeyAction = onKeyAction
                    )

                    Spacer(Modifier.height(8.dp))

                    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeContent))
                }
            }

//
//            AnimatedVisibility(focusedCalculationUuid != null) {
//                Toolbar(Modifier.padding(bottom = 16.dp))
//            }
//
//
//            AnimatedVisibility(WindowInsets.isImeVisible && focusedCalculationUuid != null) {
//                QuickKeys(onKey = onQuickKeyPressed)
//            }


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
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
    CalculatorScreenContent(
        TextFieldValue(""),
        "",
        ""
    )
}