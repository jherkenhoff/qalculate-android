package com.jherkenhoff.qalculate.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import com.jherkenhoff.qalculate.model.CalculatorAction
import com.jherkenhoff.qalculate.ui.calculator.CalculatorKeyButtonActionLabel
import com.jherkenhoff.qalculate.model.UserPreferences
import com.jherkenhoff.qalculate.ui.calculator.CalculatorKeyButtonActionLabel.*

class CalcActionLabelMapper (
    private val userPreferences: UserPreferences
) {
    operator fun invoke(action: CalculatorAction) : CalculatorKeyButtonActionLabel {
        return when (action) {
            is CalculatorAction.ClearAll -> Text("AC")
            is CalculatorAction.DeleteChars -> {
                if (action.nChars < 0) Icon(
                    Icons.AutoMirrored.Filled.Backspace,
                    null
                )
                else Text("DEL")
            }
            is CalculatorAction.InsertText -> Text(action.label)
            is CalculatorAction.MoveCursor -> {
                if (action.nChars < 0)
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null)
                else
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
            }
            is CalculatorAction.SubmitCalculation -> Icon(
                Icons.AutoMirrored.Filled.KeyboardReturn,
                null
            )
            is CalculatorAction.TraverseHistory -> {
                if (action.nEntries < 0)
                    Icon(Icons.AutoMirrored.Filled.Undo, "Undo")
                else
                    Icon(Icons.AutoMirrored.Filled.Redo, "Redo")
            }
            is CalculatorAction.InsertDivisionSymbol -> Text(userPreferences.getDivisionSignString())
            is CalculatorAction.InsertMultiplicationSymbol -> Text(userPreferences.getMultiplicationSignString())
            is CalculatorAction.InsertDecimalSymbol -> Text(userPreferences.getDecimalSeparatorString())
            is CalculatorAction.StoreAsVariable -> Text("→ ${action.name}")
            is CalculatorAction.AddCalculation -> if (action.direction == CalculatorAction.AddCalculation.Direction.ABOVE) Text("Add above") else Text("Add below")
            is CalculatorAction.DeleteCalculation -> Text("Delete")
        }
    }
}