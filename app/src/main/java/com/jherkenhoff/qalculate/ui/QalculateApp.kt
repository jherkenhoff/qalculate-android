package com.jherkenhoff.qalculate.ui

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.navigation.compose.rememberNavController
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

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                NavigationDrawer(
                    "unints",
                    onCalculatorClick = { navController.navigate(Calculator); coroutineScope.launch { drawerState.close() } },
                    onUnitsClick = { navController.navigate(Units); coroutineScope.launch { drawerState.close() } },
                    onAboutClick = { navController.navigate(About); coroutineScope.launch { drawerState.close() } },
                    modifier = Modifier.zIndex(1000f)
                )}
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
