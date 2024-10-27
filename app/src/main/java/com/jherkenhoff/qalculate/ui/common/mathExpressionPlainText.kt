package com.jherkenhoff.qalculate.ui.common

fun mathExpressionPlainText(
    mathExpressionString: String
): String {
    return Regex("""<.*?>|(&[a-z]+;)+""").replace(mathExpressionString, "")
}