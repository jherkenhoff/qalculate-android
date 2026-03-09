package com.jherkenhoff.qalculate.model

import androidx.compose.ui.graphics.vector.ImageVector

data class KeyPositionSpec(
    val row: Int,
    val col: Int,
    val rowSpan: Int = 1,
    val colSpan: Int = 1,
)

data class KeypadSection(
    val rows: Int,
    val cols: Int,
    val keys: List<Pair<KeyPositionSpec, CalcKey>>,
    val aspectRatio: Float
)

data class KeypadDefinition(
    val name: String,
    val icon: ImageVector,
    val sections: List<KeypadSection>,
    val imeEnabled: Boolean
)