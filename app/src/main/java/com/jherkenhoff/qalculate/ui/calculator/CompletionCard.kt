package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jherkenhoff.qalculate.domain.model.BaseUnit
import com.jherkenhoff.qalculate.domain.model.ExpressionItem
import com.jherkenhoff.qalculate.domain.model.ExpressionItemName

@Composable
fun CompletionCard(
    expressionItem: ExpressionItem,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick
    ) {
        Column() {
            Text(expressionItem.names.firstOrNull { it.abbreviation }?.name
                ?: expressionItem.names.firstOrNull { it.reference }?.name
                ?: expressionItem.names.firstOrNull()?.name
                ?: ""
            )
            Text(expressionItem.title)
        }
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    CompletionCard(
        BaseUnit(
            title = "Tesla",
            names = listOf(
                ExpressionItemName(
                    "T",
                    abbreviation = true,
                    reference = true,
                    caseSensitive = true
                ),
                ExpressionItemName("tesla"),
                ExpressionItemName("teslas", plural = true)
            ),
            description = "",
            category = "Magnetism/Magnetic Flux Density",
            system = "SI"
        )
    )
}