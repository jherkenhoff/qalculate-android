package com.jherkenhoff.qalculate.model

import androidx.compose.ui.text.AnnotatedString

sealed interface CalcAction {
    data class InsertText(
        val label: AnnotatedString,
        val preCursorText: String,
        val postCursorText: String = "",
        val selectionPolicy: SelectionPolicy = SelectionPolicy.REPLACE
    ) : CalcAction {
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

    data object InsertMultiplicationSymbol : CalcAction
    data object InsertDivisionSymbol : CalcAction
    data object InsertDecimalSymbol : CalcAction
    data class DeleteChars(val nChars: Int) : CalcAction
    data object ClearAll: CalcAction
    data object SubmitCalculation: CalcAction
    data class MoveCursor(val nChars: Int): CalcAction
    data class TraverseHistory(val nEntries: Int): CalcAction
    data class StoreAsVariable(val name: String): CalcAction
}