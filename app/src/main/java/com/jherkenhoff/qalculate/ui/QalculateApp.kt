package com.jherkenhoff.qalculate.ui

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jherkenhoff.qalculate.ui.calculator.NavigationDrawer
import com.jherkenhoff.qalculate.ui.theme.QalculateTheme
import kotlinx.coroutines.launch


@Composable
fun QalculateApp() {
    QalculateTheme(dynamicColor = false) {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            QalculateNavigationActions(navController)
        }

        val coroutineScope = rememberCoroutineScope()
        val keyboardController = LocalSoftwareKeyboardController.current
        val drawerState = rememberDrawerState(
            initialValue = DrawerValue.Closed,
            confirmStateChange = {
                if (it == DrawerValue.Open) {
                    keyboardController?.hide()
                }
                true
            }
        )

        val currentBackStackEntry by navController.currentBackStackEntryAsState()

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                NavigationDrawer(
                    currentBackStackEntry?.destination?.hierarchy?.any{it.hasRoute(NavDestinations.Calculator::class)} == true,
                    false,
                    false,
                    currentBackStackEntry?.destination?.hierarchy?.any{it.hasRoute(NavDestinations.Units::class)} == true,
                    false,
                    onCalculatorClick = { navController.navigate(NavDestinations.Calculator); coroutineScope.launch { drawerState.close() } },
                    onUnitsClick = { navController.navigate(NavDestinations.Units); coroutineScope.launch { drawerState.close() } },
                    onAboutClick = { navController.navigate(NavDestinations.About); coroutineScope.launch { drawerState.close() } },
                )
            }
        ) {
            QalculateNavGraph(
                navController = navController,
                openDrawer = { coroutineScope.launch { drawerState.open() } },
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    QalculateApp()
}
