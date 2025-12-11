package com.jherkenhoff.qalculate.ui.calculator

import android.content.ClipData
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.ui.common.mathExpressionFormatter
import com.jherkenhoff.qalculate.ui.common.mathExpressionPlainText
import kotlinx.coroutines.launch
import kotlin.math.max

private const val overflowFraction = 0.9f

@Composable
fun CalculationHistoryItem(
    input: String,
    parsed: String,
    result: String,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {}
) {
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    SubcomposeLayout { constraints ->

        val inputPlaceable = subcompose("input") {
            Text(
                input,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                    .combinedClickable(
                        interactionSource = null,
                        indication = null,
                        onClick = {},
                        onLongClick = {
                            scope.launch {
                                clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(null, input)))
                            }
                        }
                    )
            )
        }[0].measure(constraints.copyMaxDimensions().copy(maxWidth = (constraints.maxWidth*overflowFraction).toInt()))

        inputPlaceable[FirstBaseline]

        val resultSectionPlaceable = subcompose("resultSection") {
            ResultSection(
                result,
                onDeleteClick = onDeleteClick,
            )
        }[0].measure(constraints.copyMaxDimensions().copy(maxWidth = (constraints.maxWidth*overflowFraction).toInt()))

        val overflow =  inputPlaceable.width + resultSectionPlaceable.width > constraints.maxWidth

        val totalHeight = if (overflow) inputPlaceable.height + resultSectionPlaceable.height else max(inputPlaceable.height, resultSectionPlaceable.height)

        layout(constraints.maxWidth, totalHeight) {
            if (overflow) {
                inputPlaceable.place(0, 0)
                resultSectionPlaceable.place(constraints.maxWidth-resultSectionPlaceable.width, inputPlaceable.height)
            } else {
                inputPlaceable.place(0, (resultSectionPlaceable.height-inputPlaceable.height)/2)
                resultSectionPlaceable.place(constraints.maxWidth-resultSectionPlaceable.width, 0)
            }
        }
    }
}

@Composable
private fun ResultSection(
    resultText: String,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {}
) {
    var menuOpen by remember { mutableStateOf(false) }

    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Row(
            Modifier
                .weight(1f, fill=false)
                .combinedClickable(
                    interactionSource = null,
                    indication = null,
                    onClick = {},
                    onLongClick = {
                        scope.launch {
                            clipboard.setClipEntry(ClipEntry(
                                ClipData.newPlainText(null, mathExpressionPlainText(resultText))
                            ))
                        }
                    }
                )
        ) {
            Text(
                "= ",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.alignByBaseline(),
            )
            Text(
                mathExpressionFormatter(resultText),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.alignByBaseline()
            )
        }

        Box(
        ) {
            IconButton(
                onClick = { menuOpen = true }
            ) {
                Icon(
                    Icons.Outlined.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Menu(
                menuOpen,
                onDismissRequest = { menuOpen = false },
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
private fun Menu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onDeleteClick: () -> Unit = {}
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text("Delete") },
            leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) },
            onClick = { onDismissRequest(); onDeleteClick() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
    CalculationHistoryItem(
        "",
        "0",
        "0"
    )
}

@Preview(showBackground = true)
@Composable
private fun ShortPreview() {
    CalculationHistoryItem(
        "1km + 5m",
        "1 kilometer + 5 meter",
        "1.005 m"
    )
}

@Preview(showBackground = true)
@Composable
private fun OverflowPreview() {
    CalculationHistoryItem(
        "boltzmann * planck",
        "boltzmann*planck",
        "9.1482771E-57 second*joule^2/kelvin"
    )
}

@Preview(showBackground = true)
@Composable
private fun OverflowOverflowPreview() {
    CalculationHistoryItem(
        "boltzmann * planck",
        "boltzmann + planck",
        "6.626 070 15 × 10^(−34) Joule·seconds + 13.806 49 peta joule / terra kelvin"
    )
}