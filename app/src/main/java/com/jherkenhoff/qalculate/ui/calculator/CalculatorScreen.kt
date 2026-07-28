package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jherkenhoff.qalculate.domain.AutocompleteResult
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.CalculatorAction
import com.jherkenhoff.qalculate.model.UndoState
import com.jherkenhoff.qalculate.model.UserPreferences
import com.jherkenhoff.qalculate.ui.PreviewData
import com.jherkenhoff.qalculate.ui.common.CalcActionLabelMapper
import com.jherkenhoff.qalculate.ui.theme.QalculateTheme
import kotlinx.coroutines.launch


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
        calculationListData = viewModel.calculationListData.collectAsStateWithLifecycle().value,
        activeKeypadIndex = viewModel.activeKeypadIndex.collectAsStateWithLifecycle().value,
        userPreferences = viewModel.userPreferences.collectAsStateWithLifecycle().value,
        onUserPreferencesChanged = viewModel::updateUserPreferences,
        onKeyAction = viewModel::handleKeyAction,
        autocompleteResult = viewModel.autocompleteResult.collectAsStateWithLifecycle().value,
        undoState = viewModel.undoState.collectAsStateWithLifecycle().value,
        onInputFieldValueChange = { viewModel.updateInput(it, true) },
        onDeleteCalculation = viewModel::deleteCalculation,
        onMenuClick = openDrawer,
        onSettingsClick = openSettings,
        onAutocompleteClick = viewModel::acceptAutocomplete,
        onActiveKeypadIndexChanged = viewModel::setActiveKeypadIndex,
        onActiveCalculationChanged = viewModel::setActiveCalculationId,
        onCalculationReorder = viewModel::reorderCalculation,
        onCalculationReorderFinished = viewModel::persistCalculationOrder
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
    activeKeypadIndex: Int,
    userPreferences: UserPreferences,
    onUserPreferencesChanged : (UserPreferences) -> Unit,
    calculationListData: CalculationListData,
    autocompleteResult: AutocompleteResult,
    undoState: UndoState<TextFieldValue>,
    onKeyAction: (CalculatorAction) -> Unit = { },
    onInputFieldValueChange: (TextFieldValue) -> Unit = { },
    onDeleteCalculation: (Long) -> Unit = { },
    onAutocompleteClick: (AutocompleteItem) -> Unit = { },
    onMenuClick: () -> Unit = {  },
    onSettingsClick: () -> Unit = {  },
    onActiveKeypadIndexChanged: (Int) -> Unit = {},
    onActiveCalculationChanged: (Long) -> Unit = {},
    onCalculationReorder: (Int, Int) -> Unit = {_, _, ->},
    onCalculationReorderFinished: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val localDensity = LocalDensity.current

    val imeHeight = WindowInsets.ime.getBottom(localDensity)
    val imeFullyHidden = imeHeight == 0

    var keypadVisible by remember { mutableStateOf(true) }

    var autocompleteDismissed by remember { mutableStateOf(false) }
    if (autocompleteResult.relevantText.isEmpty()) {
        autocompleteDismissed = false
    }
    val internalAutocompleteResult = if (autocompleteDismissed) AutocompleteResult() else autocompleteResult

    val calculationListState = rememberCalculationListState()

    LaunchedEffect(activeKeypadIndex) {
        calculationListState.animateScrollToActiveCalculation()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {  },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu,
                            stringResource(com.jherkenhoff.qalculate.R.string.open_navigation_menu)
                        )
                    }
                },
                actions = {
                    CalculatorChips(
                        userPreferences
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    ) { innerPadding ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {

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
                        isCalculationSnapped = !calculationListState.isFreeScrolling,
                        onAction = onKeyAction,
                        onAutocompleteClick = onAutocompleteClick,
                        onAutocompleteDismiss = { autocompleteDismissed = true },
                        onScrollToActiveCalculationClick = {
                            scope.launch {
                                calculationListState.animateScrollToActiveCalculation()
                            }
                        },
                        onScrollToLastCalculationClick = {
                            scope.launch {
                                onActiveCalculationChanged(calculationListData.items.last().id)
                                calculationListState.animateScrollToLastCalculation()
                            }
                        },
                    )
                },
                topContent = { padding ->
                    val fadeHeightPx = padding.toFloatPx()

                    CalculationList(
                        calculationListData = calculationListData,
                        calculationListState = calculationListState,
                        activeCalculationInput = inputTextFieldValue,
                        activeCalculationParsed = parsedString,
                        activeCalculationResult = resultString,
                        padding = padding,
                        interceptKeyboard = !keypads[activeKeypadIndex].imeEnabled,
                        userPreferences = userPreferences,
                        onUserpreferencesChanged = onUserPreferencesChanged,
                        onDeleteClick = onDeleteCalculation,
                        onActiveCalculationChanged = onActiveCalculationChanged,
                        onActiveCalculationInputChange = onInputFieldValueChange,
                        onCalculationDragged = onCalculationReorder,
                        onCalculationDragStopped = onCalculationReorderFinished,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 6.dp)
                            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                            .drawWithContent {
                                drawContent()
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        0f to Color.White,
                                        1f to Color.Transparent,
                                        startY = size.height - fadeHeightPx,
                                        endY = size.height
                                    ), blendMode = BlendMode.DstIn
                                )
                            },
                    )
                },
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
            ) {
                Column {
                    AnimatedVisibility(keypadVisible) {
                        Keypad(
                            keypads[activeKeypadIndex].sections,
                            CalcActionLabelMapper(userPreferences),
                            onKeyAction = onKeyAction,
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Spacer(
                        Modifier.height(
                            innerPadding.calculateBottomPadding()
                        )
                    )
                }
            }
        }
    }
}


@Preview(name = "Light Mode", showSystemUi = true, device = Devices.DEFAULT)
@Preview(name = "Dark Mode", showSystemUi = true, device = Devices.DEFAULT, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DefaultPreview() {
    var activeKeypadIndex by remember { mutableIntStateOf(0) }

    QalculateTheme() {
        CalculatorScreenContent(
            TextFieldValue("c"),
            "SpeedOfLight",
            "299.792 458 km/ms",
            calculationListData = CalculationListData(PreviewData.calculationList, 0),
            activeKeypadIndex = activeKeypadIndex,
            userPreferences = UserPreferences(),
            onUserPreferencesChanged = {},
            autocompleteResult = AutocompleteResult(),
            undoState = UndoState<TextFieldValue>(),
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
            calculationListData = CalculationListData(PreviewData.calculationList.slice(0..0), 0),
            userPreferences = UserPreferences(),
            onUserPreferencesChanged = {},
            autocompleteResult = AutocompleteResult(),
            undoState = UndoState<TextFieldValue>(),
        )
    }
}