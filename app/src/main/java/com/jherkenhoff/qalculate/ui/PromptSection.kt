package com.jherkenhoff.qalculate.ui

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.ui.theme.QalculateTheme

@Composable
fun PromptSection(viewModel: MainViewModel = viewModel()) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal=20.dp, vertical=20.dp)
    ) {
        Spacer(
            Modifier.windowInsetsBottomHeight(
                WindowInsets.systemBars
            )
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ){
            //TextButton(onClick = {}){
            //    Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.top_sheet_menu))
            //}
            Spacer(modifier = Modifier.weight(1.0f))
            ModeLabelButton("RAD")
            ModeLabelButton("DEC")
            ModeLabelButton("EXACT")
        }

        BasicTextField(
            value = viewModel.inputString.value,
            onValueChange = viewModel::setInput,
            modifier= Modifier.fillMaxWidth().padding(top=10.dp),
            textStyle = MaterialTheme.typography.titleLarge.copy(color=MaterialTheme.colorScheme.onSurface)
        )
        Divider(
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text=viewModel.parsedString.value,
            style= MaterialTheme.typography.bodySmall.copy(color=MaterialTheme.colorScheme.onSurface),
            modifier= Modifier.fillMaxWidth()
        )
        Text(
            text=viewModel.resultString.value,
            textAlign= TextAlign.Right,
            style= MaterialTheme.typography.headlineLarge.copy(color=MaterialTheme.colorScheme.onSurface),
            modifier= Modifier.fillMaxWidth().padding(top=30.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    QalculateTheme {
        PromptSection()
    }
}