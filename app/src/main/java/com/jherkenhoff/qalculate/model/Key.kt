package com.jherkenhoff.qalculate.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardBackspace
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.ui.graphics.vector.ImageVector


sealed class KeyLabel {
    data class Text(val text: String) : KeyLabel()
    data class Icon(val icon: ImageVector, val description: String) : KeyLabel()
}

sealed class KeyAction {
    data class InsertText(val preCursorText: String, val postCursorText: String = "") : KeyAction()
    data class Backspace(val nChars: Int = 1): KeyAction()
    class ClearAll(): KeyAction()
    class Return(): KeyAction()
    class MoveCursor(val chars: Int): KeyAction()
}

enum class KeyRole {
    NUMBER, OPERATOR, SYSTEM
}

data class Key (
    val label : KeyLabel,
    val role: KeyRole,
    val clickAction : KeyAction? = null,
    val longClickAction : KeyAction? = null,
    val width: Int = 1
) {
    companion object {
        fun simpleTypeKey(text: String, role: KeyRole): Key {
            return Key(KeyLabel.Text(text), role, clickAction = KeyAction.InsertText(text))
        }
    }
}

object Keys {
    val key0 = Key.simpleTypeKey("0", KeyRole.NUMBER)
    val key1 = Key.simpleTypeKey("1", KeyRole.NUMBER)
    val key2 = Key.simpleTypeKey("2", KeyRole.NUMBER)
    val key3 = Key.simpleTypeKey("3", KeyRole.NUMBER)
    val key4 = Key.simpleTypeKey("4", KeyRole.NUMBER)
    val key5 = Key.simpleTypeKey("5", KeyRole.NUMBER)
    val key6 = Key.simpleTypeKey("6", KeyRole.NUMBER)
    val key7 = Key.simpleTypeKey("7", KeyRole.NUMBER)
    val key8 = Key.simpleTypeKey("8", KeyRole.NUMBER)
    val key9 = Key.simpleTypeKey("9", KeyRole.NUMBER)

    val keyDecimal = Key(KeyLabel.Text("."), KeyRole.NUMBER, KeyAction.InsertText("."))
    val keyAns = Key(KeyLabel.Text("ans"), KeyRole.OPERATOR)
    val keyBracketOpen = Key(KeyLabel.Text("("), KeyRole.OPERATOR, KeyAction.InsertText("("))
    val keyBracketClose = Key(KeyLabel.Text(")"), KeyRole.OPERATOR, KeyAction.InsertText(")"))
    val keyPlus = Key(KeyLabel.Text("+"), KeyRole.OPERATOR, KeyAction.InsertText("+"))
    val keyMinus = Key(KeyLabel.Text("-"), KeyRole.OPERATOR, KeyAction.InsertText("-"))
    val keyMultiply = Key(KeyLabel.Text("×"), KeyRole.OPERATOR, KeyAction.InsertText("×"))
    val keyDivide = Key(KeyLabel.Text("÷"), KeyRole.OPERATOR, KeyAction.InsertText("÷"))
    val keyPower = Key(KeyLabel.Text("^"), KeyRole.OPERATOR, KeyAction.InsertText("^"))
    val keySqrt = Key(KeyLabel.Text("√"), KeyRole.OPERATOR, KeyAction.InsertText("√"))
    val keyUnderscore = Key(KeyLabel.Text("_"), KeyRole.OPERATOR, KeyAction.InsertText("_"))
    val keyEqual = Key(KeyLabel.Text("="), KeyRole.OPERATOR, KeyAction.InsertText("="))
    val keyPi = Key(KeyLabel.Text("π"), KeyRole.OPERATOR, KeyAction.InsertText("π"))
    val keyEuler = Key(KeyLabel.Text("e"), KeyRole.OPERATOR, KeyAction.InsertText("e"))
    val keyReturn = Key(KeyLabel.Icon(Icons.AutoMirrored.Default.KeyboardReturn, "Return"), KeyRole.SYSTEM, KeyAction.Return(), width = 2)
    val keyBackspace = Key(KeyLabel.Icon(Icons.AutoMirrored.Default.KeyboardBackspace, "Backspace"), KeyRole.SYSTEM, KeyAction.Backspace(), longClickAction = KeyAction.ClearAll())
    val keyClearAll = Key(KeyLabel.Text("AC"), KeyRole.SYSTEM, KeyAction.ClearAll())
    val keyBlank = Key(KeyLabel.Text(""), KeyRole.OPERATOR)

    val keyLeft = Key(KeyLabel.Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft, description = "Move cursor left"), KeyRole.SYSTEM, clickAction = KeyAction.MoveCursor(-1))
    val keyRight = Key(KeyLabel.Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft, description = "Move cursor right"), KeyRole.SYSTEM, clickAction = KeyAction.MoveCursor(1))


    val keySin = Key(KeyLabel.Text("sin"), KeyRole.OPERATOR, KeyAction.InsertText("sin(", ")"))
}