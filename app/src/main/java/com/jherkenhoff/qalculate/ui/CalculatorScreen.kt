package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jherkenhoff.qalculate.data.model.CalculationHistoryItem
import com.jherkenhoff.qalculate.ui.theme.TopSheetShape
import java.time.LocalDateTime

@Composable
fun CalculatorScreen(viewModel: MainViewModel = viewModel()) {
    val calculationHistory = viewModel.calculationHistory.collectAsState()

    CalculatorScreenContent(
        input = viewModel.inputTextFieldValue.value,
        onInputChanged = viewModel::updateInput,
        onQuickKeyPressed = viewModel::onQuickKeyPressed,
        calculationHistory = calculationHistory.value,
        parsedString = viewModel.parsedString.value,
        resultString = viewModel.resultString.value,
        onCalculationSubmit = viewModel::submitCalculation
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreenContent(
    input: TextFieldValue,
    onInputChanged: (TextFieldValue) -> Unit,
    onQuickKeyPressed: (String) -> Unit,
    calculationHistory: List<CalculationHistoryItem>,
    parsedString: String,
    resultString: String,
    onCalculationSubmit: () -> Unit
) {

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
                    IconButton(onClick = { /*TODO*/ }) {
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
    ) {
        innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding),
        ) {
            CalculationList(
                calculationHistory,
                parsedString,
                resultString,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            InputBar(
                textFieldValue = input,
                onValueChange = onInputChanged,
                onFocused = {},
                focusState = false,
                onSubmit = { onCalculationSubmit() },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            QuickKeys(onKey = onQuickKeyPressed)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {

    val testCalculationHistory = listOf(
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

    CalculatorScreenContent(
        input = TextFieldValue("1+1"),
        onInputChanged = {},
        onQuickKeyPressed = {},
        calculationHistory = testCalculationHistory,
        parsedString = "1+1",
        resultString = "2",
        onCalculationSubmit = {}
    )
}