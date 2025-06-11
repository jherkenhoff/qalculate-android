package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CalculationDivider(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(vertical = 6.dp)
            .height(16.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}