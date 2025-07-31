package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jherkenhoff.qalculate.domain.AutocompleteResult
import com.jherkenhoff.qalculate.model.AutocompleteItem
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
    // val autocompleteResult by viewModel.autocompleteResult.collectAsStateWithLifecycle()

    CalculatorScreenContent(
        onQuickKeyPressed = viewModel::insertText,
        calculations = viewModel.calculations.collectAsState().value,
        focusedCalculationUuid = viewModel.focusedCalculationUuid.collectAsStateWithLifecycle().value,
        autocompleteResult = viewModel.autocompleteResult.collectAsStateWithLifecycle().value,
        onInputFieldValueChange = viewModel::updateCalculation,
        onDeleteCalculation = viewModel::deleteCalculation,
        onCalculationSubmit = viewModel::submitCalculation,
        onMenuClick = openDrawer,
        onSettingsClick = openSettings,
        onCalculationFocusChange = viewModel::onCalculationFocusChange,
        onAutocompleteClick = viewModel::acceptAutocomplete
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalSharedTransitionApi::class, ExperimentalLayoutApi::class
)
@Composable
fun CalculatorScreenContent(
    onQuickKeyPressed: (String, String) -> Unit,
    calculations: Map<UUID, Calculation>,
    focusedCalculationUuid: UUID?,
    autocompleteResult: AutocompleteResult? = null,
    onInputFieldValueChange: (UUID, TextFieldValue) -> Unit = {_, _ -> },
    onDeleteCalculation: (UUID) -> Unit = {},
    onCalculationSubmit: (UUID) -> Unit = {},
    onCalculationFocusChange: (UUID?) -> Unit = {},
    onAutocompleteClick: (AutocompleteItem) -> Unit = {},
    onMenuClick: () -> Unit = {  },
    onSettingsClick: () -> Unit = {  }
) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    fun deleteCalculationWithSnackbar(uuid: UUID) {
        scope.launch {
            snackbarHostState.showSnackbar(
                "Deleted calculation " + calculations[uuid]?.input?.text,
                actionLabel = "Undo",
                withDismissAction = true,
                duration = SnackbarDuration.Short
            )
        }

        onDeleteCalculation(uuid)
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CalculatorTopBar(
                onMenuClick = onMenuClick,
                onSettingsClick = onSettingsClick
            )

            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier.weight(1f)
            ) {
                CalculationList(
                    calculations,
                    focusedCalculationUuid,
                    autocompleteResult = autocompleteResult,
                    onInputFieldValueChange = onInputFieldValueChange,
                    onDeleteClick = { deleteCalculationWithSnackbar(it) },
                    onSubmit = onCalculationSubmit,
                    onCalculationFocusChange = onCalculationFocusChange,
                    onAutocompleteClick = onAutocompleteClick,
                    modifier = Modifier.fillMaxHeight()
                )

                SnackbarHost(snackbarHostState)
            }


            AnimatedVisibility(focusedCalculationUuid != null) {
                Toolbar(Modifier.padding(bottom = 16.dp))
            }


            AnimatedVisibility(WindowInsets.isImeVisible && focusedCalculationUuid != null) {
                QuickKeys(onKey = onQuickKeyPressed)
            }

            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeContent))
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
        onQuickKeyPressed = {_, _ ->},
        calculations = testCalculationHistory,
        focusedCalculationUuid = null,
        onCalculationSubmit = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
    CalculatorScreenContent(
        onQuickKeyPressed = {_, _ ->},
        calculations = emptyMap(),
        focusedCalculationUuid = null,
        onCalculationSubmit = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun AutocompletePreview() {
    CalculatorScreenContent(
        onQuickKeyPressed = {_, _ ->},
        calculations = testCalculationHistory,
        focusedCalculationUuid = null,
        onCalculationSubmit = {},
    )
}