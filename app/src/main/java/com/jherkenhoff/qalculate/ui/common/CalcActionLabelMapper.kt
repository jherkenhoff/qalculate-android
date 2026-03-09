package com.jherkenhoff.qalculate.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import com.jherkenhoff.qalculate.model.CalculatorAction
import com.jherkenhoff.qalculate.ui.calculator.CalculatorKeyButtonActionLabel
import com.jherkenhoff.qalculate.model.UserPreferences

class CalcActionLabelMapper (
    private val userPreferences: UserPreferences
) {
    operator fun invoke(action: CalculatorAction) : CalculatorKeyButtonActionLabel {
        return when (action) {
            is CalculatorAction.ClearAll -> CalculatorKeyButtonActionLabel.Text("AC")
            is CalculatorAction.DeleteChars -> {
                if (action.nChars < 0) CalculatorKeyButtonActionLabel.Icon(
                    Icons.AutoMirrored.Filled.Backspace,
                    null
                )
                else CalculatorKeyButtonActionLabel.Text("DEL")
            }
            is CalculatorAction.InsertText -> CalculatorKeyButtonActionLabel.Text(action.label)
            is CalculatorAction.MoveCursor -> {
                if (action.nChars < 0) CalculatorKeyButtonActionLabel.Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    null
                )
                else CalculatorKeyButtonActionLabel.Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null)
            }
            is CalculatorAction.SubmitCalculation -> CalculatorKeyButtonActionLabel.Icon(
                Icons.AutoMirrored.Filled.KeyboardReturn,
                null
            )
            is CalculatorAction.TraverseHistory -> {
                if (action.nEntries < 0) CalculatorKeyButtonActionLabel.Icon(
                    Icons.AutoMirrored.Filled.Undo,
                    "Undo"
                )
                else CalculatorKeyButtonActionLabel.Icon(Icons.AutoMirrored.Filled.Redo, "Redo")
            }
            is CalculatorAction.InsertDivisionSymbol -> CalculatorKeyButtonActionLabel.Text(userPreferences.getDivisionSignString())
            is CalculatorAction.InsertMultiplicationSymbol -> CalculatorKeyButtonActionLabel.Text(userPreferences.getMultiplicationSignString())
            is CalculatorAction.InsertDecimalSymbol -> CalculatorKeyButtonActionLabel.Text(userPreferences.getDecimalSeparatorString())
            is CalculatorAction.StoreAsVariable -> CalculatorKeyButtonActionLabel.Text("→ ${action.name}")
        }
    }
}