package com.jherkenhoff.qalculate.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object QalculateDestinations {
    const val CALCULATOR = "calculator"
    const val FUNCTIONS  = "functions"
    const val VARIABLES  = "variables"
    const val UNITS      = "units"
    const val ABOUT      = "about"
}

class QalculateNavigationActions(navController: NavHostController) {

    val navigateToCalculator: () -> Unit = {
        navController.navigate(QalculateDestinations.CALCULATOR) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateToUnits: () -> Unit = {
        navController.navigate(QalculateDestinations.UNITS) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}