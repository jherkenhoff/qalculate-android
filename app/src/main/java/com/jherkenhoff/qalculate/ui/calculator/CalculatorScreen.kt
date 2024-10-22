package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButtonColors
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jherkenhoff.qalculate.data.ScreenSettingsRepository
import com.jherkenhoff.qalculate.data.model.CalculationHistoryItem
import com.jherkenhoff.qalculate.domain.AutocompleteItem
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime


@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel(),
    openDrawer: () -> Unit = {}
) {
    val calculationHistory = viewModel.calculationHistory.collectAsState()

    CalculatorScreenContent(
        input = { viewModel.inputTextFieldValue },
        onInputChanged = viewModel::updateInput,
        onQuickKeyPressed = viewModel::insertText,
        onDelKeyPressed = viewModel::removeLastChar,
        onACKeyPressed = viewModel::clearAll,
        calculationHistory = calculationHistory.value,
        parsedString = { viewModel.parsedString },
        resultString = { viewModel.resultString },
        onCalculationSubmit = viewModel::submitCalculation,
        onAutocompleteClick = viewModel::acceptAutocomplete,
        openDrawer = openDrawer,
        autocompleteList = { viewModel.autocompleteResult.items },
        autocompleteText = { viewModel.autocompleteResult.relevantText }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CalculatorScreenContent(
    input: () -> TextFieldValue,
    onInputChanged: (TextFieldValue) -> Unit,
    onQuickKeyPressed: (String, String) -> Unit,
    onDelKeyPressed: () -> Unit,
    onACKeyPressed: () -> Unit,
    calculationHistory: List<CalculationHistoryItem>,
    parsedString: () -> String,
    resultString: () -> String,
    onCalculationSubmit: () -> Unit = {},
    autocompleteText: () -> String = {""},
    autocompleteList: () -> List<AutocompleteItem> = { emptyList() },
    onAutocompleteClick: (String) -> Unit = {},
    openDrawer: () -> Unit = {  }
) {

    val screenSettingsRepository = ScreenSettingsRepository(LocalContext.current)
    var isAltKeyboardOpen = screenSettingsRepository.isAltKeyboardOpen.collectAsState(true).value

    var autocompleteDismissed by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }

                },
                actions = {
                    SuggestionChip(onClick = { /*TODO*/ }, label = { Text("DEG") })
                    SuggestionChip(onClick = { /*TODO*/ }, label = { Text("Exact") })
                    SuggestionChip(onClick = { /*TODO*/ }, label = { Text("Exp.") })
                }

            )
        },
        modifier = Modifier.imePadding(),
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding),
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                CalculationList(
                    calculationHistory,
                    parsedString,
                    resultString
                )
                this@Column.AnimatedVisibility(!autocompleteDismissed && autocompleteList().isNotEmpty()) {
                    AutocompleteList(
                        autocompleteText,
                        entries = autocompleteList,
                        onEntryClick = onAutocompleteClick,
                        onDismiss = { autocompleteDismissed = true },
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .heightIn(max = 300.dp)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                FilledIconToggleButton(
                    checked = isAltKeyboardOpen,
                    onCheckedChange = {
                        isAltKeyboardOpen = !isAltKeyboardOpen
                        runBlocking {
                            screenSettingsRepository.saveAltKeyboardOpen(isAltKeyboardOpen)
                        }
                    },
                    colors = IconToggleButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        contentColor = MaterialTheme.colorScheme.primary,
                        checkedContainerColor = MaterialTheme.colorScheme.primary,
                        checkedContentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Filled.Calculate, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(8.dp))
                InterceptPlatformTextInput(
                    interceptor = { request, nextHandler ->
                        if (!isAltKeyboardOpen) {
                            nextHandler.startInputMethod(request)
                        } else {
                            awaitCancellation()
                        }
                    },
                ) {
                    InputBar(
                        textFieldValue = input,
                        onValueChange = onInputChanged,
                        onSubmit = { onCalculationSubmit() },
                    )
                }
            }

            AnimatedContent(targetState = isAltKeyboardOpen) {
                if (it) {
                    AltKeyboard(
                        onKey = { text -> onQuickKeyPressed(text, "") },
                        onDel = onDelKeyPressed,
                        onAC = onACKeyPressed,
                        onSubmit = onCalculationSubmit,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    )
                } else {
                    QuickKeys(
                        onKey = onQuickKeyPressed
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
    ),
    CalculationHistoryItem(
        LocalDateTime.now().minusDays(1).minusHours(2),
        "1m + 1m",
        "1 m + 1 m",
        "2 m"
    ),
    CalculationHistoryItem(
        LocalDateTime.now().minusMinutes(20),
        "1m + 1m",
        "1 m + 1 m",
        "2 m"
    )
)

@Preview
@Composable
private fun DefaultPreview() {
    CalculatorScreenContent(
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
private fun AutocompletePreview() {

    val list = listOf(
        AutocompleteItem("Tesla", "M", "T"),
        AutocompleteItem("Thomson cross section", "M", "T"),
        AutocompleteItem("Terabyte", "M", "T"),
        AutocompleteItem("Planck temperature", "M", "T"),
    )

    CalculatorScreenContent(
        input = { TextFieldValue("1*t") },
        onInputChanged = {},
        onQuickKeyPressed = {_, _ ->},
        onDelKeyPressed = {},
        onACKeyPressed = {},
        calculationHistory = testCalculationHistory,
        parsedString = { "" },
        resultString = { "" },
        onCalculationSubmit = {},
        autocompleteText = { "t" },
        autocompleteList = { list }
    )
}
