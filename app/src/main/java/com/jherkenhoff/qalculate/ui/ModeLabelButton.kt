package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.ui.theme.QalculateTheme


@Composable
fun ModeLabelButton(
    text : String,
    modifier : Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(color=MaterialTheme.colorScheme.onSecondary),
        textAlign = TextAlign.Center,
        modifier = modifier
            .clip(CircleShape)
            .clickable{  }
            .background(MaterialTheme.colorScheme.secondary, CircleShape)
            .padding(horizontal=6.dp, vertical=4.dp)
    )
}

@Preview(showBackground = false)
@Composable
private fun DefaultPreview() {
    QalculateTheme {
        ModeLabelButton("DEG")
    }
}