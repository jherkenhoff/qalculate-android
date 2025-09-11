package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
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
                .padding(vertical = 36.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .shadow(10.dp, shape = CircleShape)
            )
        }
        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.navigation_calculator), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface) },
            selected = calculatorActive,
            onClick = onCalculatorClick,
            icon = { Icon(Icons.Filled.Calculate, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        HorizontalDivider(
            Modifier
                .padding(horizontal = 28.dp, vertical = 18.dp),
            color = MaterialTheme.colorScheme.outline
        )
        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.navigation_functions), textDecoration = TextDecoration.LineThrough, color = MaterialTheme.colorScheme.onSurfaceVariant) },
            selected = functionsActive,
            onClick = onFunctionsClick,
            icon = { Icon(Icons.Outlined.Functions, contentDescription = null, tint = MaterialTheme.colorScheme.secondary) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.navigation_variables), textDecoration = TextDecoration.LineThrough, color = MaterialTheme.colorScheme.onSurfaceVariant) },
            selected = variablesActive,
            onClick = onVariablesClick,
            icon = { Icon(Icons.Outlined.Pin, contentDescription = null, tint = MaterialTheme.colorScheme.secondary) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.navigation_units), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface) },
            selected = unitsActive,
            onClick = onUnitsClick,
            icon = { Icon(Icons.Outlined.Straighten, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.navigation_datasets), textDecoration = TextDecoration.LineThrough, color = MaterialTheme.colorScheme.onSurfaceVariant) },
            selected = datasetsActive,
            onClick = onDatasetsClick,
            icon = { Icon(Icons.Outlined.Dataset, contentDescription = null, tint = MaterialTheme.colorScheme.secondary) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(
            Modifier
                .padding(horizontal = 28.dp)
                .padding(top = 18.dp),
            color = MaterialTheme.colorScheme.outline
        )
        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.navigation_about), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface) },
            selected = false,
            onClick = onAboutClick,
            icon = { Icon(Icons.Filled.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.navigation_settings), textDecoration = TextDecoration.LineThrough, color = MaterialTheme.colorScheme.onSurfaceVariant) },
            selected = false,
            onClick = onSettingsClick,
            icon = { Icon(Icons.Filled.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.secondary) },
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

