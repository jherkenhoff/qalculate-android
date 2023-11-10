package com.jherkenhoff.qalculate.ui

import android.text.Layout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.ui.theme.QalculateTheme

@Composable
fun RowScope.NumButton(text: String, modifier: Modifier = Modifier) {
    Button(onClick = { /*TODO*/ }, modifier=modifier.weight(1f).fillMaxSize()) {
        Text(text = text, style=MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun Numpad(modifier: Modifier = Modifier) {
    Column(
        modifier=modifier.fillMaxSize().padding(horizontal=20.dp, vertical=20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier=Modifier.weight(1f).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp))
        {
            NumButton("7")
            NumButton("8")
            NumButton("9")
            NumButton("⌫")
            NumButton("AC")
        }
        Row(
            modifier=Modifier.weight(1f).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            NumButton("4")
            NumButton("5")
            NumButton("6")
            NumButton("*")
            NumButton("/")
        }
        Row(
            modifier=Modifier.weight(1f).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            NumButton("1")
            NumButton("2")
            NumButton("3")
            NumButton("+")
            NumButton("-")
        }
        Row(
            modifier=Modifier.weight(1f).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            NumButton("0")
            NumButton(".")
            NumButton("E")
            NumButton("Ans")
            NumButton("=")
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