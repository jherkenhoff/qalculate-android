package com.jherkenhoff.qalculate.ui

import android.text.Layout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun NumButton(text: String, modifier: Modifier = Modifier) {
    Button(onClick = { /*TODO*/ }, modifier=modifier) {
        Text(text = text, style=MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun Numpad(modifier: Modifier = Modifier) {
    Column(modifier=modifier.padding(horizontal=20.dp, vertical=20.dp)) {
        Row(modifier=Modifier.fillMaxWidth()){
            NumButton("7", modifier = Modifier.fillMaxWidth().weight(1f))
            NumButton("8", modifier = Modifier.fillMaxWidth().weight(1f))
            NumButton("9", modifier = Modifier.fillMaxWidth().weight(1f))
        }
        Row {
            NumButton("4", modifier = Modifier.fillMaxWidth().weight(1f))
            NumButton("5", modifier = Modifier.fillMaxWidth().weight(1f))
            NumButton("6", modifier = Modifier.fillMaxWidth().weight(1f))
        }
        Row {
            NumButton("1", modifier = Modifier.fillMaxWidth().weight(1f))
            NumButton("2", modifier = Modifier.fillMaxWidth().weight(1f))
            NumButton("3", modifier = Modifier.fillMaxWidth().weight(1f))
        }
        Row {
            NumButton("0", modifier = Modifier.fillMaxWidth().weight(1f))
            NumButton(".", modifier = Modifier.fillMaxWidth().weight(1f))
            NumButton("E", modifier = Modifier.fillMaxWidth().weight(1f))
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