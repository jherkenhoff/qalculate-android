package com.jherkenhoff.qalculate.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.jherkenhoff.qalculate.ui.CalculatorScreen
import com.jherkenhoff.qalculate.ui.theme.QalculateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            QalculateTheme(dynamicColor = false) {
                CalculatorScreen()
            }
        }
    }
}