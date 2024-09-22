package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jherkenhoff.qalculate.data.ScreenSettingsRepository
import com.jherkenhoff.qalculate.data.model.CalculationHistoryItem
import com.jherkenhoff.qalculate.domain.AutocompleteItem
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
        autocompleteList = { viewModel.autocompleteList },
        autocompleteText = { viewModel.autocompleteText }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreenContent(
    input: () -> TextFieldValue,
    onInputChanged: (TextFieldValue) -> Unit,
    onQuickKeyPressed: (String) -> Unit,
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
                    .weight(1f)
            ) {
                CalculationList(
                    calculationHistory,
                    parsedString,
                    resultString,
                    bottomSpacing = 64.dp
                )
                Column(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                ) {

                    AnimatedVisibility(autocompleteList().isNotEmpty()) {
                            AutocompleteList(
                                autocompleteText,
                                entries = autocompleteList,
                                onEntryClick = onAutocompleteClick,
                                modifier = Modifier
                                    .padding(vertical = 16.dp)
                                    .heightIn(max = 300.dp)
                            )
                    }
                    InputBar(
                        textFieldValue = input,
                        onValueChange = onInputChanged,
                        onSubmit = { onCalculationSubmit() },
                        altKeyboardEnabled = isAltKeyboardOpen,
                        onKeyboardToggleClick = {
                            isAltKeyboardOpen = !isAltKeyboardOpen
                            runBlocking {
                                screenSettingsRepository.saveAltKeyboardOpen(isAltKeyboardOpen)
                            }
                        },
                    )
                }
            }

            if (isAltKeyboardOpen) {
                AltKeyboard(
                    onKey = onQuickKeyPressed,
                    onDel = onDelKeyPressed,
                    onAC = onACKeyPressed,
                    onSubmit = onCalculationSubmit
                )
            } else {
                SupplementaryBar(
                    onKey = onQuickKeyPressed,
                    autocompleteItems = { emptyList() }, // autocompleteList
                    onAutocompleteClick = onAutocompleteClick
                )
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
        onQuickKeyPressed = {},
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
        onQuickKeyPressed = {},
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
