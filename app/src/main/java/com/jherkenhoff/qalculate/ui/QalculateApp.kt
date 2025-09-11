package com.jherkenhoff.qalculate.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jherkenhoff.qalculate.ui.theme.QalculateTheme
import kotlinx.coroutines.launch


@Composable
fun QalculateApp() {
    QalculateTheme(dynamicColor = true) {
        val navController = rememberNavController()
        QalculateNavGraph(
            navController = navController
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    QalculateApp()
}
