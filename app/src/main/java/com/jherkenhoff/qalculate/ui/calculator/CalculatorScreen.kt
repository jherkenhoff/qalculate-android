package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jherkenhoff.qalculate.domain.AutocompleteResult
import com.jherkenhoff.qalculate.model.Calculation
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel(),
    openDrawer: () -> Unit = { },
    openSettings: () -> Unit = { }
) {
    val autocompleteResult by viewModel.autocompleteResult.collectAsStateWithLifecycle()
    val inputTextFieldValue by viewModel.inputTextFieldValue.collectAsStateWithLifecycle()
    val parsedString by viewModel.parsedString.collectAsStateWithLifecycle()
    val resultString by viewModel.resultString.collectAsStateWithLifecycle()

    CalculatorScreenContent(
        autocompleteResult = autocompleteResult,
        input = { inputTextFieldValue },
        altKeyboardVisible = viewModel.altKeyboardOpen.collectAsState(false).value,
        onInputChanged = viewModel::updateInput,
        onQuickKeyPressed = viewModel::insertText,
        onDelKeyPressed = viewModel::removeLastChar,
        onACKeyPressed = viewModel::clearInput,
        calculations = viewModel.calculations.collectAsState().value,
        parsedString = { parsedString },
        resultString = { resultString },
        onInputFieldValueChange = viewModel::onInputFieldValueChange,
        onDeleteCalculation = viewModel::deleteCalculation,
        onCalculationSubmit = viewModel::submitCalculation,
        onAltKeyboardToggle = viewModel::toggleAltKeyboard,
        onAutocompleteClick = viewModel::acceptAutocomplete,
        openDrawer = openDrawer,
        openSettings = openSettings
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalSharedTransitionApi::class, ExperimentalLayoutApi::class
)
@Composable
fun CalculatorScreenContent(
    autocompleteResult: AutocompleteResult,
    input: () -> TextFieldValue,
    altKeyboardVisible: Boolean,
    onInputChanged: (TextFieldValue) -> Unit,
    onQuickKeyPressed: (String, String) -> Unit,
    onDelKeyPressed: () -> Unit,
    onACKeyPressed: () -> Unit,
    calculations: Map<UUID, Calculation>,
    parsedString: () -> String,
    resultString: () -> String,
    onInputFieldValueChange: (UUID, TextFieldValue) -> Unit = {_, _ -> },
    onDeleteCalculation: (UUID) -> Unit = {},
    onCalculationSubmit: () -> Unit = {},
    onAltKeyboardToggle: (Boolean) -> Unit = {},
    onAutocompleteClick: (String, String) -> Unit = {_, _ ->},
    openDrawer: () -> Unit = {  },
    openSettings: () -> Unit = {  }
) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    fun deleteCalculationWithSnackbar(uuid: UUID) {
        scope.launch {

            snackbarHostState.showSnackbar(
                "Deleted calculation " + calculations[uuid]?.input?.text,
                actionLabel = "Undo",
                withDismissAction = true
            )
        }

        onDeleteCalculation(uuid)
    }

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.weight(1f))

            Box(contentAlignment = Alignment.BottomCenter) {
                CalculationList(
                    calculations,
                    onInputFieldValueChange = onInputFieldValueChange,
                    onDeleteClick = { deleteCalculationWithSnackbar(it) }
                )

                SnackbarHost(snackbarHostState)
            }

            QuickKeys(onKey = onQuickKeyPressed)
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeContent))
            // AutocompleteBar(autocompleteResult.items)
        }
    }
}


private val testCalculationHistory = mapOf(
    UUID.randomUUID() to Calculation(
        LocalDateTime.now().minusDays(10),
        LocalDateTime.now().minusDays(10),
        TextFieldValue("1m + 1m"),
        "1 m + 1 m",
        "2 m"
    ),
    UUID.randomUUID() to Calculation(
        LocalDateTime.now().minusDays(10),
        LocalDateTime.now().minusDays(10),
        TextFieldValue("1m + 1m"),
        "1 m + 1 m",
        "2 m"
    )
)

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    CalculatorScreenContent(
        autocompleteResult = AutocompleteResult(),
        input = { TextFieldValue("1+1") },
        altKeyboardVisible = false,
        onInputChanged = {},
        onQuickKeyPressed = {_, _ ->},
        onDelKeyPressed = {},
        onACKeyPressed = {},
        calculations = testCalculationHistory,
        parsedString = { "1+1" },
        resultString = { "2" },
        onCalculationSubmit = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
    CalculatorScreenContent(
        autocompleteResult = AutocompleteResult(),
        input = { TextFieldValue("") },
        altKeyboardVisible = false,
        onInputChanged = {},
        onQuickKeyPressed = {_, _ ->},
        onDelKeyPressed = {},
        onACKeyPressed = {},
        calculations = emptyMap(),
        parsedString = { "0" },
        resultString = { "0" },
        onCalculationSubmit = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun AutocompletePreview() {
    CalculatorScreenContent(
        autocompleteResult = AutocompleteResult(),
        input = { TextFieldValue("1*t") },
        altKeyboardVisible = false,
        onInputChanged = {},
        onQuickKeyPressed = {_, _ ->},
        onDelKeyPressed = {},
        onACKeyPressed = {},
        calculations = testCalculationHistory,
        parsedString = { "" },
        resultString = { "" },
        onCalculationSubmit = {},
    )
}