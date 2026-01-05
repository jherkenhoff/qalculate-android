package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    onClearCalculationHistoryClicked: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {

    var clearHistoryDialogOpen by remember { mutableStateOf(false) }

    ModalDrawerSheet(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .shadow(6.dp, shape = CircleShape)
            )
        }
        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.navigation_calculator)) },
            selected = calculatorActive,
            onClick = onCalculatorClick,
            icon = { Icon(Icons.Filled.Calculate, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        HorizontalDivider(
            Modifier.padding(horizontal = 25.dp, vertical = 15.dp)
        )
//        NavigationDrawerItem(
//            label = { Text(text = stringResource(R.string.navigation_functions), textDecoration = TextDecoration.LineThrough) },
//            selected = functionsActive,
//            onClick = onFunctionsClick,
//            icon = { Icon(Icons.Outlined.Functions, contentDescription = null) },
//            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//        )
//        NavigationDrawerItem(
//            label = { Text(text = stringResource(R.string.navigation_variables), textDecoration = TextDecoration.LineThrough) },
//            selected = variablesActive,
//            onClick = onVariablesClick,
//            icon = { Icon(Icons.Outlined.Pin, contentDescription = null) },
//            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//        )
//        NavigationDrawerItem(
//            label = { Text(text = stringResource(R.string.navigation_units)) },
//            selected = unitsActive,
//            onClick = onUnitsClick,
//            icon = { Icon(Icons.Outlined.Straighten, contentDescription = null) },
//            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//        )
//        NavigationDrawerItem(
//            label = { Text(text = stringResource(R.string.navigation_datasets), textDecoration = TextDecoration.LineThrough) },
//            selected = datasetsActive,
//            onClick = onDatasetsClick,
//            icon = { Icon(Icons.Outlined.Dataset, contentDescription = null) },
//            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//        )
        Spacer(modifier = Modifier.weight(1f))
        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.clear_history)) },
            selected = false,
            onClick = { clearHistoryDialogOpen = true },
            icon = { Icon(Icons.Default.Delete, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        HorizontalDivider(Modifier.padding(horizontal = 25.dp))
        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.navigation_settings)) },
            selected = false,
            onClick = onSettingsClick,
            icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(text = stringResource(R.string.navigation_about)) },
            selected = false,
            onClick = onAboutClick,
            icon = { Icon(Icons.Filled.Info, contentDescription = null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }


    if (clearHistoryDialogOpen) {
        AlertDialog(
            icon = {
                Icon(Icons.Default.Delete, contentDescription = null)
            },
            title = {
                Text(text = stringResource(R.string.clear_history_dialog_title))
            },
            text = {
                Text(text = stringResource(R.string.clear_history_dialog_text))
            },
            onDismissRequest = {
                clearHistoryDialogOpen = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        clearHistoryDialogOpen = false
                        onClearCalculationHistoryClicked()
                    }
                ) {
                    Text(stringResource(R.string.dialog_delete_button))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        clearHistoryDialogOpen = false
                    }
                ) {
                    Text(stringResource(R.string.dialog_cancel_button))
                }
            }
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

