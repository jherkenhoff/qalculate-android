package com.jherkenhoff.qalculate.ui.common

fun mathExpressionPlainText(
    mathExpressionString: String
): String {
    val tokens = Regex("""<.*?>|(&[a-z]+;)+|([^<&]+)?""").findAll(mathExpressionString)

    return tokens.joinToString("") {
        when (it.value) {
            "<i>" -> ""
            "</i>" -> ""
            "<span style=\"color:#800000\">" -> ""
            "<span style=\"color:#005858\">" -> ""
            "<span style=\"color:#585800\">" -> ""
            "<span style=\"color:#008000\">" -> ""
            "</span>" -> ""
            "<sup>" -> "^("
            "</sup>" -> ")"
            "<sub>" -> ""
            "</sub>" -> ""
            "&nbsp;" -> ""
            "&lt;" -> "<"
            "&gt;" -> ">"
            "&amp;" -> "&"
            else -> it.value
        }
    }
}