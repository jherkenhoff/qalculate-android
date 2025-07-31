package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jherkenhoff.qalculate.data.model.CalculationHistoryItem
import kotlinx.coroutines.awaitCancellation
import java.time.LocalDateTime


@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel(),
    openDrawer: () -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CalculatorScreenContent(
        uiState = uiState,
        input = { viewModel.inputTextFieldValue },
        altKeyboardVisible = viewModel.altKeyboardOpen.collectAsState(false).value,
        onInputChanged = viewModel::updateInput,
        onQuickKeyPressed = viewModel::insertText,
        onDelKeyPressed = viewModel::removeLastChar,
        onACKeyPressed = viewModel::clearAll,
        calculationHistory = viewModel.calculationHistory.collectAsState().value,
        parsedString = { viewModel.parsedString },
        resultString = { viewModel.resultString },
        onCalculationSubmit = viewModel::submitCalculation,
        onAltKeyboardToggle = viewModel::toggleAltKeyboard,
        onAutocompleteClick = viewModel::acceptAutocomplete,
        onAutocompleteDismiss = viewModel::dismissAutocomplete,
        openDrawer = openDrawer,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun CalculatorScreenContent(
    uiState: CalculatorUiState,
    input: () -> TextFieldValue,
    altKeyboardVisible: Boolean,
    onInputChanged: (TextFieldValue) -> Unit,
    onQuickKeyPressed: (String, String) -> Unit,
    onDelKeyPressed: () -> Unit,
    onACKeyPressed: () -> Unit,
    calculationHistory: List<CalculationHistoryItem>,
    parsedString: () -> String,
    resultString: () -> String,
    onCalculationSubmit: () -> Unit = {},
    onAltKeyboardToggle: (Boolean) -> Unit = {},
    onAutocompleteClick: (String, String) -> Unit = {_, _ ->},
    onAutocompleteDismiss: () -> Unit = {  },
    openDrawer: () -> Unit = {  }
) {

    //val screenSettingsRepository = ScreenSettingsRepository(LocalContext.current)
    //var isAltKeyboardOpen = screenSettingsRepository.isAltKeyboardOpen.collectAsState(true).value

    var autocompleteDismissed by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {},
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }

                },
                actions = {
                    SuggestionChip(onClick = { /*TODO*/ }, enabled = false, label = { Text("DEG") })
                    Spacer(Modifier.width(8.dp))
                    SuggestionChip(onClick = { /*TODO*/ }, enabled = false, label = { Text("Exact") })
                    Spacer(Modifier.width(8.dp))
                    SuggestionChip(onClick = { /*TODO*/ }, enabled = false, label = { Text("Exp.") })
                }

            )
        },
        modifier = Modifier.imePadding(),
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding),
        ) {
            HistroyList(
                calculationHistory,
                onTextToInput = { onQuickKeyPressed(it, "") },
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            InterceptPlatformTextInput(
                interceptor = { request, nextHandler ->

                    if (!altKeyboardVisible) {
                        nextHandler.startInputMethod(request)
                    } else {
                        awaitCancellation()
                    }
                },
            ) {
                InputSheet(
                    textFieldValue = input,
                    parsed = parsedString(),
                    result = resultString(),
                    isAltKeyboardOpen = altKeyboardVisible,
                    onValueChange = onInputChanged,
                    onSubmit = { onCalculationSubmit() },
                    onToggleAltKeyboard = { onAltKeyboardToggle(!altKeyboardVisible) },
                    onClearAll = onACKeyPressed,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedContent(targetState = altKeyboardVisible) {
                if (it) {
                    AltKeyboard(
                        onKey = { text -> onQuickKeyPressed(text, "") },
                        onDel = onDelKeyPressed,
                        onAC = onACKeyPressed,
                        onSubmit = onCalculationSubmit,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    )
                } else {
                    SupplementaryBar(
                        onKey = onQuickKeyPressed,
                        autocompleteItems = { uiState.autocompleteList },
                        onAutocompleteClick = onAutocompleteClick,
                        onAutocompleteDismiss = onAutocompleteDismiss
                    )
                }
            }
        }
    }
}


private val testCalculationHistory = listOf(
    CalculationHistoryItem(
        LocalDateTime.now().minusDays(10),
        "1m + 1m",
        "1 m + 1 m",
        "2 m"
    ),
    CalculationHistoryItem(
        LocalDateTime.now().minusDays(1),
        "1m + 1m",
        "1 m + 1 m",
        "2 m"
    )
)

@Preview
@Composable
private fun DefaultPreview() {
    CalculatorScreenContent(
        uiState = CalculatorUiState(),
        input = { TextFieldValue("1+1") },
        altKeyboardVisible = false,
        onInputChanged = {},
        onQuickKeyPressed = {_, _ ->},
        onDelKeyPressed = {},
        onACKeyPressed = {},
        calculationHistory = testCalculationHistory,
        parsedString = { "1+1" },
        resultString = { "2" },
        onCalculationSubmit = {}
    )
}

@Preview
@Composable
private fun EmptyPreview() {
    CalculatorScreenContent(
        uiState = CalculatorUiState(),
        input = { TextFieldValue("") },
        altKeyboardVisible = false,
        onInputChanged = {},
        onQuickKeyPressed = {_, _ ->},
        onDelKeyPressed = {},
        onACKeyPressed = {},
        calculationHistory = emptyList(),
        parsedString = { "0" },
        resultString = { "0" },
        onCalculationSubmit = {}
    )
}


@Preview
@Composable
private fun AutocompletePreview() {

    CalculatorScreenContent(
        uiState = CalculatorUiState(),
        input = { TextFieldValue("1*t") },
        altKeyboardVisible = false,
        onInputChanged = {},
        onQuickKeyPressed = {_, _ ->},
        onDelKeyPressed = {},
        onACKeyPressed = {},
        calculationHistory = testCalculationHistory,
        parsedString = { "" },
        resultString = { "" },
        onCalculationSubmit = {},
    )
}
