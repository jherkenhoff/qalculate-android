package com.jherkenhoff.qalculate.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import com.jherkenhoff.qalculate.model.CalcAction
import com.jherkenhoff.qalculate.model.CalcActionLabel
import com.jherkenhoff.qalculate.model.UserPreferences

class CalcActionLabelMapper (
    private val userPreferences: UserPreferences
) {
    operator fun invoke(action: CalcAction) : CalcActionLabel {
        return when (action) {
            is CalcAction.ClearAll -> CalcActionLabel.Text("AC")
            is CalcAction.DeleteChars -> {
                if (action.nChars < 0) CalcActionLabel.Icon(
                    Icons.AutoMirrored.Filled.Backspace,
                    null
                )
                else CalcActionLabel.Text("DEL")
            }
            is CalcAction.InsertText -> CalcActionLabel.Text(action.label)
            is CalcAction.MoveCursor -> {
                if (action.nChars < 0) CalcActionLabel.Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    null
                )
                else CalcActionLabel.Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null)
            }
            is CalcAction.SubmitCalculation -> CalcActionLabel.Icon(
                Icons.AutoMirrored.Filled.KeyboardReturn,
                null
            )
            is CalcAction.TraverseHistory -> {
                if (action.nEntries < 0) CalcActionLabel.Icon(
                    Icons.AutoMirrored.Filled.Undo,
                    "Undo"
                )
                else CalcActionLabel.Icon(Icons.AutoMirrored.Filled.Redo, "Redo")
            }
            is CalcAction.InsertDivisionSymbol -> CalcActionLabel.Text(userPreferences.getDivisionSignString())
            is CalcAction.InsertMultiplicationSymbol -> CalcActionLabel.Text(userPreferences.getMultiplicationSignString())
            is CalcAction.InsertDecimalSymbol -> CalcActionLabel.Text(userPreferences.getDecimalSeparatorString())
            is CalcAction.StoreAsVariable -> CalcActionLabel.Text("→ ${action.name}")
        }
    }
}