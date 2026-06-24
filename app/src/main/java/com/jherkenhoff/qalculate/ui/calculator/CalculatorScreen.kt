package com.jherkenhoff.qalculate.ui.calculator

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import com.jherkenhoff.qalculate.domain.AutocompleteResult
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.CalculatorAction
import com.jherkenhoff.qalculate.model.UndoState
import com.jherkenhoff.qalculate.model.UserPreferences
import com.jherkenhoff.qalculate.ui.PreviewData
import com.jherkenhoff.qalculate.ui.common.CalcActionLabelMapper
import com.jherkenhoff.qalculate.ui.theme.QalculateTheme


@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel(),
    openDrawer: () -> Unit = { },
    openSettings: () -> Unit = { }
) {
    CalculatorScreenContent(
        inputTextFieldValue = viewModel.inputTextFieldValue.collectAsStateWithLifecycle().value,
        parsedString = viewModel.parsedString.collectAsStateWithLifecycle().value,
        resultString = viewModel.resultString.collectAsStateWithLifecycle().value,
        activeCalculationId = viewModel.activeCalculationId.collectAsStateWithLifecycle().value,
        activeKeypadIndex = viewModel.activeKeypadIndex.collectAsStateWithLifecycle().value,
        userPreferences = viewModel.userPreferences.collectAsStateWithLifecycle().value,
        onUserPreferencesChanged = viewModel::updateUserPreferences,
        calculationHistory = viewModel.calculationHistory.collectAsStateWithLifecycle().value,
        onKeyAction = viewModel::handleKeyAction,
        autocompleteResult = viewModel.autocompleteResult.collectAsStateWithLifecycle().value,
        undoState = viewModel.undoState.collectAsStateWithLifecycle().value,
        onInputFieldValueChange = { viewModel.updateInput(it, true) },
        onDeleteCalculation = viewModel::deleteCalculation,
        onMenuClick = openDrawer,
        onSettingsClick = openSettings,
        onAutocompleteClick = viewModel::acceptAutocomplete,
        onActiveKeypadIndexChanged = viewModel::setActiveKeypadIndex,
        onActiveCalculationChanged = viewModel::setActiveCalculationId
    )
}

fun Modifier.shrinkHeightAbsolute(shrinkPx: Int): Modifier = this.then(
    Modifier.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val height = (placeable.height - shrinkPx).coerceAtLeast(0)
        layout(placeable.width, height) {
            // Place at top; clip the bottom part
            placeable.place(0, 0)
        }
    }
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalSharedTransitionApi::class, ExperimentalLayoutApi::class
)
@Composable
fun CalculatorScreenContent(
    inputTextFieldValue: TextFieldValue,
    parsedString: String,
    resultString: String,
    activeCalculationId: Int,
    activeKeypadIndex: Int,
    userPreferences: UserPreferences,
    onUserPreferencesChanged : (UserPreferences) -> Unit,
    calculationHistory: List<CalculationHistoryItemData> = emptyList(),
    autocompleteResult: AutocompleteResult,
    undoState: UndoState<TextFieldValue>,
    onKeyAction: (CalculatorAction) -> Unit = { },
    onInputFieldValueChange: (TextFieldValue) -> Unit = { },
    onDeleteCalculation: (CalculationHistoryItemData) -> Unit = { },
    onAutocompleteClick: (AutocompleteItem) -> Unit = { },
    onMenuClick: () -> Unit = {  },
    onSettingsClick: () -> Unit = {  },
    onActiveKeypadIndexChanged: (Int) -> Unit = {},
    onActiveCalculationChanged: (Int) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val localDensity = LocalDensity.current

    val imeHeight = WindowInsets.ime.getBottom(localDensity)
    val imeFullyHidden = imeHeight == 0

    var autocompleteDismissed by remember { mutableStateOf(false) }
    if (autocompleteResult.relevantText.isEmpty()) {
        autocompleteDismissed = false
    }
    val internalAutocompleteResult = if (autocompleteDismissed) AutocompleteResult() else autocompleteResult

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeContent))
        CalculationList(
            calculations = calculationHistory,
            activeCalculationIdx = activeCalculationId,
            activeCalculationInput = inputTextFieldValue,
            activeCalculationParsed = parsedString,
            activeCalculationResult = resultString,
            interceptKeyboard = !keypads[activeKeypadIndex].imeEnabled,
            userPreferences = userPreferences,
            modifier = Modifier.weight(1f).padding(horizontal = 6.dp),
            onUserpreferencesChanged = onUserPreferencesChanged,
            onDeleteClick = onDeleteCalculation,
            onActiveCalculationChanged = onActiveCalculationChanged,
            onActiveCalculationInputChange = onInputFieldValueChange,
        )

        TabPanel(
            tabItems = keypads.map {
                Pair(it.icon, it.name)
            },
            activeTabItemIndex = activeKeypadIndex,
            collapse = internalAutocompleteResult.items.isNotEmpty(),
            onTabClicked = onActiveKeypadIndexChanged,
            trailingContent = {
                AuxiliaryBar(
                    internalAutocompleteResult,
                    auxiliaryActions = listOf(
                        CalculatorAction.MoveCursor(-1),
                        CalculatorAction.MoveCursor(+1),
                        CalculatorAction.TraverseHistory(-1),
                        CalculatorAction.TraverseHistory(+1),
                    ),
                    calcActionLabelMapper = CalcActionLabelMapper(userPreferences),
                    onAction = onKeyAction,
                    onAutocompleteClick = onAutocompleteClick,
                    onAutocompleteDismiss = { autocompleteDismissed = true }
                )
            },
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
        ) {
            Column {
                AnimatedContent(
                    activeKeypadIndex
                ) {
                    Keypad(
                        keypads[it].sections,
                        CalcActionLabelMapper(userPreferences),
                        onKeyAction = onKeyAction,
                    )
                }
                Spacer(Modifier.height(6.dp))
                Spacer(Modifier.height(WindowInsets.safeContent.getBottom(LocalDensity.current).toDp()))
            }
        }
    }
}


@Preview(name = "Light Mode", showSystemUi = true, device = Devices.DEFAULT)
@Preview(name = "Dark Mode", showSystemUi = true, device = Devices.DEFAULT, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DefaultPreview() {
    var activeKeypadIndex by remember { mutableIntStateOf(0) }

    QalculateTheme() {
        CalculatorScreenContent(
            TextFieldValue("c"),
            "SpeedOfLight",
            "299.792 458 km/ms",
            PreviewData.calculationList.last().id,
            activeKeypadIndex = activeKeypadIndex,
            userPreferences = UserPreferences(),
            onUserPreferencesChanged = {},
            autocompleteResult = AutocompleteResult(),
            undoState = UndoState<TextFieldValue>(),
            calculationHistory = PreviewData.calculationList,
            onActiveKeypadIndexChanged = { activeKeypadIndex = it }
        )
    }
}


@Preview(showSystemUi = true, device = Devices.DEFAULT)
@Composable
private fun SingleCalculationPreview() {

    QalculateTheme() {
        CalculatorScreenContent(
            TextFieldValue("c"),
            "SpeedOfLight",
            "299.792 458 km/ms",
            0,
            activeKeypadIndex = 0,
            userPreferences = UserPreferences(),
            onUserPreferencesChanged = {},
            autocompleteResult = AutocompleteResult(),
            undoState = UndoState<TextFieldValue>(),
            calculationHistory = PreviewData.calculationList.slice(0..0),
        )
    }
}