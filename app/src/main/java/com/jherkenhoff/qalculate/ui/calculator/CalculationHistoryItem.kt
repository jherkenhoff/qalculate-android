package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import com.jherkenhoff.qalculate.ui.common.mathExpressionFormatter
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

    SubcomposeLayout { constraints ->

        val inputPlaceable = subcompose("input") {
            Text(
                input
            )
        }[0].measure(constraints.copy(maxWidth = (constraints.maxWidth*overflowFraction).toInt()))

        val resultSectionPlaceable = subcompose("resultSection") {
            ResultSection(
                result,
                onDeleteClick = onDeleteClick,
            )
        }[0].measure(constraints.copy(maxWidth = (constraints.maxWidth*overflowFraction).toInt()))

        val overflow =  inputPlaceable.width + resultSectionPlaceable.width > constraints.maxWidth

        val totalHeight = if (overflow) inputPlaceable.height + resultSectionPlaceable.height else max(inputPlaceable.height, resultSectionPlaceable.height)

        layout(constraints.maxWidth, totalHeight) {
            inputPlaceable.place(0, 0)
            if (overflow) {
                resultSectionPlaceable.place(constraints.maxWidth-resultSectionPlaceable.width, inputPlaceable.height)
            } else {
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

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Row(
            Modifier.weight(1f, fill=false)
        ) {
            Text(
                "= ",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.alignByBaseline()
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
            text = { Text("Pin") },
            leadingIcon = { Icon(Icons.Outlined.PushPin, contentDescription = null) },
            onClick = { /* Do something... */ }
        )
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