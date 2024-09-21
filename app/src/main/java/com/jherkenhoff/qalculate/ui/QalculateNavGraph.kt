package com.jherkenhoff.qalculate.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
sealed class NavDestinations{
    @Serializable data object Calculator
    @Serializable data object Units
    @Serializable data object About
}


@Composable
fun QalculateNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    openDrawer: () -> Unit = {},
    startDestination: NavDestinations.Calculator = NavDestinations.Calculator,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable<NavDestinations.Calculator> {
            CalculatorScreen(
                viewModel = hiltViewModel<CalculatorViewModel>(),
                openDrawer = openDrawer,
            )
        }

        composable<NavDestinations.Units> {
            UnitsScreen(
                viewModel = hiltViewModel<UnitsViewModel>(),
                openDrawer = openDrawer,
            )
        }

        dialog<NavDestinations.About> {
            AboutCard()
        }

    }
}