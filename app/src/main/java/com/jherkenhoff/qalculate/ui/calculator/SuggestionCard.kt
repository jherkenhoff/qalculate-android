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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SuggestionCard(
    title: String,
    abbreviations: List<String>,
    description: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        shadowElevation = 3.dp,
        modifier = modifier.sizeIn(maxWidth = 180.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHighest
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                abbreviations.joinToString(separator = ", "),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.alpha(0.7f)
            )
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}



@Preview
@Composable
private fun DefaultPreview() {
    SuggestionCard(
        "Tesla",
        listOf("tesla", "T"),
        "Magnetic Flux Density"
    )
}