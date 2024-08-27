package com.jherkenhoff.qalculate.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun QalculateNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    openDrawer: () -> Unit = {},
    startDestination: String = QalculateDestinations.CALCULATOR,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(route = QalculateDestinations.CALCULATOR) {
            CalculatorScreen(
                viewModel = hiltViewModel<MainViewModel>(),
                openDrawer = openDrawer,
            )
        }
    }
}