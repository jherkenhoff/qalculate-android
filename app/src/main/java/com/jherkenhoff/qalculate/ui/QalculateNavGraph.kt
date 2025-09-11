package com.jherkenhoff.qalculate.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.jherkenhoff.qalculate.ui.calculator.CalculatorScreen
import com.jherkenhoff.qalculate.ui.calculator.CalculatorViewModel
import com.jherkenhoff.qalculate.ui.units.UnitsScreen
import com.jherkenhoff.qalculate.ui.units.UnitsViewModel
import kotlinx.serialization.Serializable

@Serializable
sealed class NavDestinations{
    @Serializable data object Calculator
    @Serializable data object About
}


@Composable
fun QalculateNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
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
                onAboutClick = { navController.navigate(NavDestinations.About) },
                onSettingsClick = { /* TODO: Implement settings if needed */ }
            )
        }
        dialog<NavDestinations.About> {
            AboutCard()
        }
    }
}