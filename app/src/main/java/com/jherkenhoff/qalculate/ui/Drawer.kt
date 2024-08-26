package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.R


@Composable
fun Drawer(

) {
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(30.dp))
        NavigationDrawerItem(
            label = { Text(text = "Calculator") },
            selected = true,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Filled.Calculate, contentDescription = "Calculator icon") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        HorizontalDivider(Modifier.padding(horizontal = 25.dp).padding(top = 15.dp))
        NavigationDrawerItem(
            label = { Text(text = "Functions") },
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Filled.Functions, contentDescription = "Functions icon") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(text = "Variables") },
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Filled.Pin, contentDescription = "Functions icon") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(Modifier.padding(horizontal = 25.dp).padding(top = 15.dp))
        NavigationDrawerItem(
            label = { Text(text = "About") },
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Filled.Info, contentDescription = "Settings icon") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text(text = "Settings") },
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings icon") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    Drawer()
}