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

@Serializable data object Calculator

@Serializable data object Units
@Serializable data object About

@Composable
fun QalculateNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    openDrawer: () -> Unit = {},
    startDestination: Calculator = Calculator,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable<Calculator> {
            CalculatorScreen(
                viewModel = hiltViewModel<CalculatorViewModel>(),
                openDrawer = openDrawer,
            )
        }

        composable<Units> {
            UnitsScreen(
                viewModel = hiltViewModel<UnitsViewModel>(),
                openDrawer = openDrawer,
            )
        }

        dialog<About> {
            AboutCard()
        }

    }
}