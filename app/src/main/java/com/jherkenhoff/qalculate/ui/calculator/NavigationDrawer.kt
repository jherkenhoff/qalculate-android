package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Dataset
import androidx.compose.material.icons.outlined.Functions
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.R

// TODO: Passing so many repetitive parameters seems not very elegant.
@Composable
fun NavigationDrawer(
    calculatorActive: Boolean,
    functionsActive: Boolean,
    variablesActive: Boolean,
    unitsActive: Boolean,
    datasetsActive: Boolean,
    modifier: Modifier = Modifier,
    onCalculatorClick: () -> Unit = {},
    onFunctionsClick: () -> Unit = {},
    onVariablesClick: () -> Unit = {},
    onUnitsClick: () -> Unit = {},
    onDatasetsClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    ModalDrawerSheet(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Qalculate logo",
                modifier = Modifier
                    .size(80.dp)
                    .shadow(6.dp, shape = CircleShape)
            )
        }
        NavigationDrawerItem(
            label = { Text(text = "Calculator") },
            selected = calculatorActive,
            onClick = onCalculatorClick,
            icon = { Icon(Icons.Filled.Calculate, contentDescription = "Calculator icon") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        HorizontalDivider(
            Modifier
                .padding(horizontal = 25.dp, vertical = 15.dp))
        NavigationDrawerItem(
            label = { Text(text = "Functions") },
            selected = functionsActive,
            onClick = onFunctionsClick,
            icon = { Icon(Icons.Outlined.Functions, contentDescription = "Functions icon") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(text = "Variables") },
            selected = variablesActive,
            onClick = onVariablesClick,
            icon = { Icon(Icons.Outlined.Pin, contentDescription = "Variables icon") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(text = "Units") },
            selected = unitsActive,
            onClick = onUnitsClick,
            icon = { Icon(Icons.Outlined.Straighten, contentDescription = "Units icon") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(text = "Datasets") },
            selected = datasetsActive,
            onClick = onDatasetsClick,
            icon = { Icon(Icons.Outlined.Dataset, contentDescription = "Datasets icon") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(
            Modifier
                .padding(horizontal = 25.dp)
                .padding(top = 15.dp))
        NavigationDrawerItem(
            label = { Text(text = "About") },
            selected = false,
            onClick = onAboutClick,
            icon = { Icon(Icons.Filled.Info, contentDescription = "About icon") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(text = "Settings") },
            selected = false,
            onClick = onSettingsClick,
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings icon") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    NavigationDrawer(
        true,
        false,
        false,
        false,
        false
    )
}

