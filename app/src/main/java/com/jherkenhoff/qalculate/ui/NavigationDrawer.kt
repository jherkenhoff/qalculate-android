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

@Composable
fun NavigationDrawer(
    currentPage: String,
    modifier: Modifier = Modifier,
    onNavigation: (String) -> Unit = {},
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
            selected = currentPage == QalculateDestinations.CALCULATOR,
            onClick = onCalculatorClick,
            icon = { Icon(Icons.Filled.Calculate, contentDescription = "Calculator icon") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        HorizontalDivider(
            Modifier
                .padding(horizontal = 25.dp, vertical = 15.dp))
        NavigationDrawerItem(
            label = { Text(text = "Functions") },
            selected = currentPage == QalculateDestinations.FUNCTIONS,
            onClick = onFunctionsClick,
            icon = { Icon(Icons.Outlined.Functions, contentDescription = "Functions icon") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(text = "Variables") },
            selected = currentPage == QalculateDestinations.VARIABLES,
            onClick = onVariablesClick,
            icon = { Icon(Icons.Outlined.Pin, contentDescription = "Variables icon") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(text = "Units") },
            selected = currentPage == QalculateDestinations.UNITS,
            onClick = onUnitsClick,
            icon = { Icon(Icons.Outlined.Straighten, contentDescription = "Units icon") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(text = "Datasets") },
            selected = false,
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
        QalculateDestinations.UNITS
    )
}

