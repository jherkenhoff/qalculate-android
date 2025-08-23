package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jherkenhoff.qalculate.domain.AutocompleteResult
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.Calculation
import com.jherkenhoff.qalculate.model.KeyAction
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
        onKeyAction = viewModel::handleKeyAction,
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
    onKeyAction: (KeyAction) -> Unit,
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

    var keyboardEnable by remember { mutableStateOf(false) }

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
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeContent))


            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Column() {
                    Text("Moin",
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    HorizontalDivider()
                    Text("Moin", Modifier.padding(horizontal = 16.dp))
                    Text("Moin",
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth()
                    )
                }
            }

//            CalculatorTopBar(
//                onMenuClick = onMenuClick,
//                onSettingsClick = onSettingsClick
//            )

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

            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AnimatedVisibility(!keyboardEnable) {
                        SecondaryKeypad(onKeyAction = onKeyAction)
                    }
                    PrimaryKeypad(onKeyAction = onKeyAction)

                    AuxiliaryBar(
                        autocompleteResult = autocompleteResult,
                        keyboardEnable = keyboardEnable,
                        onAutocompleteClick = onAutocompleteClick,
                        onKeyboardEnableChange = {keyboardEnable = it}
                    )

                    Spacer(Modifier.height(8.dp))
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
        onKeyAction = { },
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
        onKeyAction = { },
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
        onKeyAction = { },
        onQuickKeyPressed = {_, _ ->},
        calculations = testCalculationHistory,
        focusedCalculationUuid = null,
        onCalculationSubmit = {},
    )
}