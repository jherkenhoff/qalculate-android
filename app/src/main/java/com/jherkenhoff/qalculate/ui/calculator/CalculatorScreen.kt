package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.ui.unit.sp
// import removed: androidx.compose.ui.text.style.Shadow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
//
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
    onAboutClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CalculatorScreenContent(
        viewModel = viewModel,
        uiState = uiState,
        input = { viewModel.inputTextFieldValue },
        onInputChanged = viewModel::updateInput,
        onQuickKeyPressed = viewModel::insertText,
        onDelKeyPressed = viewModel::removeLastChar,
        onACKeyPressed = viewModel::clearAll,
        calculationHistory = viewModel.calculationHistory.collectAsState().value,
        parsedString = { viewModel.parsedString },
        resultString = { viewModel.resultString },
        onCalculationSubmit = viewModel::submitCalculation,
        onAutocompleteClick = viewModel::acceptAutocomplete,
        onAutocompleteDismiss = viewModel::dismissAutocomplete,
        onAboutClick = onAboutClick,
        onSettingsClick = onSettingsClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun CalculatorScreenContent(
    viewModel: CalculatorViewModel?,
    uiState: CalculatorUiState,
    input: () -> TextFieldValue,
    onInputChanged: (TextFieldValue) -> Unit,
    onQuickKeyPressed: (String, String) -> Unit,
    onDelKeyPressed: () -> Unit,
    onACKeyPressed: () -> Unit,
    calculationHistory: List<CalculationHistoryItem>,
    parsedString: () -> String,
    resultString: () -> String,
    onCalculationSubmit: () -> Unit = {},
    onAutocompleteClick: (String, String) -> Unit = {_, _ ->},
    onAutocompleteDismiss: () -> Unit = {  },
    onAboutClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {

    //val screenSettingsRepository = ScreenSettingsRepository(LocalContext.current)
    //var isAltKeyboardOpen = screenSettingsRepository.isAltKeyboardOpen.collectAsState(true).value

    var autocompleteDismissed by remember { mutableStateOf(false) }

    var showInfo by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "Qalculate!",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1
                    )
                },
                actions = {
                    IconButton(onClick = { showInfo = true }) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "About"
                        )
                    }
                }
            )
        },
        modifier = Modifier.imePadding(),
    ) { innerPadding ->
        if (showInfo) {
            androidx.compose.ui.window.Dialog(onDismissRequest = { showInfo = false }) {
                com.jherkenhoff.qalculate.ui.AboutCard()
            }
        }
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .fillMaxSize()
        ) {
            androidx.compose.material3.Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = MaterialTheme.shapes.large,
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(2.dp)
                    ) {
                CalculationList(
                    calculationHistory = calculationHistory,
                    currentParsed = parsedString,
                    currentResult = resultString,
                    modifier = Modifier.fillMaxSize()
                )
            }
            // Input field
            androidx.compose.material3.Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
            ) {
                InterceptPlatformTextInput(
                    interceptor = { request, nextHandler ->
                        nextHandler.startInputMethod(request)
                    },
                ) {
                    val focusRequester = remember { androidx.compose.ui.focus.FocusRequester() }
                    val lastFocusState = remember { mutableStateOf(false) }
                    val placeholdeVisible = input().text.isEmpty()
                    InputSheet(
                        textFieldValue = input,
                        parsed = parsedString(),
                        result = resultString(),
                        onValueChange = onInputChanged,
                        onSubmit = { onCalculationSubmit() },
                        onClearAll = onACKeyPressed,
                        focusRequester = focusRequester,
                        lastFocusState = lastFocusState,
                        placeholdeVisible = placeholdeVisible,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            // Supplementary bar
            SupplementaryBar(
                onKey = onQuickKeyPressed,
                autocompleteItems = { uiState.autocompleteList },
                onAutocompleteClick = onAutocompleteClick,
                onAutocompleteDismiss = onAutocompleteDismiss
            )
        }
    }
}


private val testCalculationHistory = run {
    val now = java.time.LocalDateTime.now()
    listOf(
        CalculationHistoryItem(
            now.minusDays(10).toString(),
            "1m + 1m",
            "1 m + 1 m",
            "2 m"
        ),
        CalculationHistoryItem(
            now.minusDays(1).toString(),
            "1m + 1m",
            "1 m + 1 m",
            "2 m"
        )
    )
}

@Preview
@Composable
private fun DefaultPreview() {
    CalculatorScreenContent(
        viewModel = null,
        uiState = CalculatorUiState(),
        input = { TextFieldValue("1+1") },
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
        viewModel = null,
        uiState = CalculatorUiState(),
        input = { TextFieldValue("") },
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
        viewModel = null,
        uiState = CalculatorUiState(),
        input = { TextFieldValue("1*t") },
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
