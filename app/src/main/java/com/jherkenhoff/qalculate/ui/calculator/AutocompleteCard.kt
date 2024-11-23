package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.AutocompleteType

@Composable
fun AutocompleteCard(
    item: AutocompleteItem,
    modifier: Modifier = Modifier
) {
    val highlightColor = when (item.type) {
        AutocompleteType.FUNCTION -> MaterialTheme.colorScheme.primary
        AutocompleteType.UNIT -> MaterialTheme.colorScheme.secondary
        AutocompleteType.VARIABLE -> MaterialTheme.colorScheme.tertiary
        AutocompleteType.CURRENCY -> MaterialTheme.colorScheme.error
    }
    Surface(
        shape = RoundedCornerShape(6.dp),
        shadowElevation = 3.dp,
        modifier = modifier.sizeIn(maxWidth = 180.dp),
        color = highlightColor
    ) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            modifier = modifier.padding(bottom=5.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHighest
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    item.name,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.alpha(0.7f)
                )
                Text(
                    item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

    }
}