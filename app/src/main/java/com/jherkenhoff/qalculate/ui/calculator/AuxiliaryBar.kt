package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Input
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Input
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.domain.AutocompleteResult
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.CalculatorAction
import com.jherkenhoff.qalculate.model.UserPreferences
import com.jherkenhoff.qalculate.ui.common.CalcActionLabelMapper

@Composable
fun AuxiliaryBar(
    autocompleteResult: AutocompleteResult,
    auxiliaryActions: List<CalculatorAction>,
    calcActionLabelMapper: CalcActionLabelMapper,
    isCalculationSnapped: Boolean,
    modifier: Modifier = Modifier,
    onAutocompleteClick: (AutocompleteItem) -> Unit = { },
    onAction: (CalculatorAction) -> Unit = { },
    onAutocompleteDismiss: () -> Unit = { },
    onScrollToActiveCalculationClick: () -> Unit = { },
    onScrollToLastCalculationClick: () -> Unit = { },
) {
    val fadeWidth = 40f

    AnimatedContent(
        autocompleteResult.items.isNotEmpty() && isCalculationSnapped,
        modifier = modifier
    ) {
        if (it) {
            Row {
                LazyRow(
                    modifier = Modifier.fillMaxHeight().weight(1f)
                        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                        .drawWithContent {
                            drawContent()
                            drawRect(brush = Brush.horizontalGradient(0f to Color.White, 1f to Color.Transparent, startX = this.size.width-fadeWidth, endX = this.size.width), blendMode = BlendMode.DstIn)
                            drawRect(brush = Brush.horizontalGradient(0f to Color.Transparent, 1f to Color.White, startX = 0f, endX = fadeWidth), blendMode = BlendMode.DstIn)
                        },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item{
                        Spacer(Modifier.width(fadeWidth.toDp()-8.dp))
                    }
                    items(autocompleteResult.items) { it ->
                        SuggestionChip(
                            label = { Text(it.title) },
                            onClick = { onAutocompleteClick(it) }
                        )
                    }
                    item{
                        Spacer(Modifier.width(fadeWidth.toDp()-8.dp))
                    }
                }

                FilledTonalIconButton(onAutocompleteDismiss) {
                    Icon(Icons.Default.Close, "Dismiss autocomplete suggestions")
                }
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                AnimatedContent(isCalculationSnapped) {
                    if (it) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            AuxiliaryIconButton(
                                Icons.AutoMirrored.Default.KeyboardArrowLeft,
                                null,
                                onClick = {},
                                roundStart = true,
                                roundEnd = false
                            )

                            AuxiliaryIconButton(
                                Icons.AutoMirrored.Default.KeyboardArrowRight,
                                null,
                                onClick = {},
                                roundStart = false,
                                roundEnd = true
                            )
                        }
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            AuxiliaryIconButton(
                                Icons.Default.VerticalAlignBottom,
                                null,
                                onClick = onScrollToLastCalculationClick,
                                roundStart = true,
                                roundEnd = false,
                                color = MaterialTheme.colorScheme.secondary
                            )

                            AuxiliaryIconButton(
                                Icons.AutoMirrored.Default.Input,
                                null,
                                onClick = onScrollToActiveCalculationClick,
                                roundStart = false,
                                roundEnd = true,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }

//                    for (action in auxiliaryActions) {
//                        FilledTonalIconButton(
//                            onClick = { onAction(action) },
//                        ) {
//                            when (val label = calcActionLabelMapper(action)) {
//                                is CalculatorKeyButtonActionLabel.Text -> Text(
//                                    label.text,
//                                    style = MaterialTheme.typography.labelLarge
//                                )
//
//                                is CalculatorKeyButtonActionLabel.Icon -> Icon(
//                                    label.icon,
//                                    label.description,
//                                    modifier = Modifier.size(MaterialTheme.typography.labelLarge.lineHeight.toDp())
//                                )
//
//                                null -> null
//                            }
//                        }
//                    }
            }
        }
    }
}

@Composable
fun AuxiliaryIconButton(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    roundStart: Boolean,
    roundEnd: Boolean,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceContainerHigh
) {
    Surface(
        onClick = onClick,
        color = color,
        shape = RoundedCornerShape(
                if (roundStart) 50 else 10,
                if (roundEnd) 50 else 10,
                if (roundEnd) 50 else 10,
                if (roundStart) 50 else 10
            ),
        modifier = modifier.widthIn(min = 48.dp)
    ) {
        Icon(
            imageVector,
            contentDescription,
            modifier = Modifier.padding(8.dp).size(18.dp)
        )
    }
}

@Preview
@Composable
private fun CalculationSnappedPreview() {
    AuxiliaryBar(
        autocompleteResult = AutocompleteResult(),
        auxiliaryActions = emptyList(),
        calcActionLabelMapper = CalcActionLabelMapper(UserPreferences()),
        isCalculationSnapped = true
    )
}

@Preview
@Composable
private fun CalculationNotSnappedPreview() {
    AuxiliaryBar(
        autocompleteResult = AutocompleteResult(),
        auxiliaryActions = emptyList(),
        calcActionLabelMapper = CalcActionLabelMapper(UserPreferences()),
        isCalculationSnapped = false
    )
}