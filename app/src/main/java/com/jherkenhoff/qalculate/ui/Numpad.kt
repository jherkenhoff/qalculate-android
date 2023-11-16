package com.jherkenhoff.qalculate.ui

import android.text.Layout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.ui.theme.QalculateTheme

@Composable
fun RowScope.NumButton(text: String, secondaryText: String? = null, modifier: Modifier = Modifier, containerColor: Color = MaterialTheme.colorScheme.secondaryContainer) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier= modifier
            .weight(1f)
            .fillMaxSize()
    ) {
        if (secondaryText != null) {
            Text(
                text = secondaryText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        FilledTonalButton(
            onClick = { /*TODO*/ },
            contentPadding = PaddingValues(0.dp),
            modifier= modifier
                .weight(1f)
                .fillMaxSize(),
            colors = ButtonDefaults.filledTonalButtonColors(containerColor = containerColor),
        ) {
            Text(text = text, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }
}

@Composable
fun Numpad(
    modifier: Modifier = Modifier
) {
    val horizontalSpacing = 10.dp
    Column(
        modifier= modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier= Modifier
                .weight(0.6f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing))
        {
            NumButton("", secondaryText="")
            NumButton("", secondaryText="")
            NumButton("", secondaryText="")
            NumButton("", secondaryText="")
            NumButton("", secondaryText="")
        }
        Row(
            modifier= Modifier
                .weight(0.6f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing))
        {
            NumButton("to", secondaryText="")
            NumButton("", secondaryText="")
            NumButton("", secondaryText="")
            NumButton("", secondaryText="")
            NumButton("", secondaryText="")
        }
        Row(
            modifier= Modifier
                .weight(0.6f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing))
        {
            NumButton("π", secondaryText="CONST")
            NumButton("e", secondaryText="ln")
            NumButton("sin", secondaryText="sin⁻¹")
            NumButton("cos", secondaryText="cos⁻¹")
            NumButton("tan", secondaryText="tan⁻¹")
        }
        Row(
            modifier= Modifier
                .weight(0.6f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing))
        {
            NumButton("x", secondaryText = "VAR")
            NumButton("√", secondaryText="")
            NumButton("^", secondaryText="")
            NumButton("(", secondaryText="")
            NumButton(")", secondaryText="")
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier= Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing))
        {
            NumButton("7")
            NumButton("8")
            NumButton("9")
            NumButton("⌫", modifier=Modifier.weight(2f), containerColor = MaterialTheme.colorScheme.errorContainer)
        }
        Row(
            modifier= Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)) {
            NumButton("4")
            NumButton("5")
            NumButton("6")
            NumButton("*", containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            NumButton("/", containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        }
        Row(
            modifier= Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)) {
            NumButton("1")
            NumButton("2")
            NumButton("3")
            NumButton("+", containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            NumButton("-", containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        }
        Row(
            modifier= Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)) {
            NumButton("0")
            NumButton(".")
            NumButton("E")
            NumButton("Ans", containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            NumButton("⏎", containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    QalculateTheme {
        Numpad()
    }
}