package com.jherkenhoff.qalculate.model

import androidx.compose.ui.text.AnnotatedString

sealed interface CalculatorAction {
    data class InsertText(
        val label: AnnotatedString,
        val preCursorText: String,
        val postCursorText: String = "",
        val selectionPolicy: SelectionPolicy = SelectionPolicy.REPLACE
    ) : CalculatorAction {
        enum class SelectionPolicy{REPLACE, SURROUND, PARENTHESES}

        constructor(label: String, preCursorText: String, postCursorText: String = "", selectionPolicy: SelectionPolicy = SelectionPolicy.REPLACE)
                : this(AnnotatedString(label), preCursorText, postCursorText, selectionPolicy)

        constructor(preCursorText: String, postCursorText: String = "", selectionPolicy: SelectionPolicy = SelectionPolicy.REPLACE)
                : this(AnnotatedString(preCursorText), preCursorText, postCursorText, selectionPolicy)

        companion object {
            fun function(label: AnnotatedString, function: String): InsertText {
                return InsertText(label, preCursorText = "$function(", postCursorText = ")", selectionPolicy = SelectionPolicy.SURROUND)
            }
            fun function(label: String, function: String): InsertText {
                return InsertText(label, preCursorText = "$function(", postCursorText = ")", selectionPolicy = SelectionPolicy.SURROUND)
            }
            fun function(function: String): InsertText {
                return function(function, function)
            }
            fun operator(label: AnnotatedString, operator: String): InsertText {
                return InsertText(label, preCursorText = operator, selectionPolicy = SelectionPolicy.PARENTHESES)
            }
            fun operator(operator: String): InsertText {
                return InsertText(operator, preCursorText = operator, selectionPolicy = SelectionPolicy.PARENTHESES)
            }
        }
    }

    data object InsertMultiplicationSymbol : CalculatorAction
    data object InsertDivisionSymbol : CalculatorAction
    data object InsertDecimalSymbol : CalculatorAction
    data class DeleteChars(val nChars: Int) : CalculatorAction
    data object ClearAll: CalculatorAction
    data object SubmitCalculation: CalculatorAction
    data class MoveCursor(val nChars: Int): CalculatorAction
    data class TraverseHistory(val nEntries: Int): CalculatorAction
    data class StoreAsVariable(val name: String?): CalculatorAction
}