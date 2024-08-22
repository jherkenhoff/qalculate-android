package com.inidamleader.ovtracker.util.compose.geometry

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import kotlin.math.roundToInt

// Those function are designed to be used in lambdas

// DP
fun Density.dpToSp(dp: Dp) = if (dp.isSpecified) dp.toSp() else TextUnit.Unspecified

fun Density.dpToFloatPx(dp: Dp) = if (dp.isSpecified) dp.toPx() else Float.NaN

fun Density.dpToIntPx(dp: Dp) = if (dp.isSpecified) dp.toPx().toInt() else 0

fun Density.dpRoundToPx(dp: Dp) = if (dp.isSpecified) dp.roundToPx() else 0

@Composable
fun Dp.toSp() = LocalDensity.current.dpToSp(this)

@Composable
fun Dp.toFloatPx() = LocalDensity.current.dpToFloatPx(this)

@Composable
fun Dp.toIntPx() = LocalDensity.current.dpToIntPx(this)

@Composable
fun Dp.roundToPx() = LocalDensity.current.dpRoundToPx(this)

fun Dp.toRecDpSize() = if (isSpecified) DpSize(this, this) else DpSize.Unspecified

fun Dp.toRecDpOffset() = if (isSpecified) DpOffset(this, this) else DpOffset.Unspecified


// TEXT UNIT
fun Density.spToDp(sp: TextUnit) = if (sp.isSpecified) sp.toDp() else Dp.Unspecified

fun Density.spToFloatPx(sp: TextUnit) = if (sp.isSpecified) sp.toPx() else Float.NaN

fun Density.spToIntPx(sp: TextUnit) = if (sp.isSpecified) sp.toPx().toInt() else 0

fun Density.spRoundToPx(sp: TextUnit) = if (sp.isSpecified) sp.roundToPx() else 0

@Composable
fun TextUnit.toDp() = LocalDensity.current.spToDp(this)

@Composable
fun TextUnit.toFloatPx() = LocalDensity.current.spToFloatPx(this)

@Composable
fun TextUnit.toIntPx() = LocalDensity.current.spToIntPx(this)

@Composable
fun TextUnit.roundToPx() = LocalDensity.current.spRoundToPx(this)


// FLOAT
fun Density.floatPxToDp(px: Float) = if (px.isFinite()) px.toDp() else Dp.Unspecified

fun Density.floatPxToSp(px: Float) = if (px.isFinite()) px.toSp() else TextUnit.Unspecified

@Composable
fun Float.toDp() = LocalDensity.current.floatPxToDp(this)

@Composable
fun Float.toSp() = LocalDensity.current.floatPxToSp(this)

fun Float.toIntPx() = if (isFinite()) toInt() else 0

fun Float.roundToPx() = if (isFinite()) roundToInt() else 0

fun Float.toRecSize() = if (isFinite()) Size(this, this) else Size.Unspecified

fun Float.toRecOffset() = if (isFinite()) Offset(this, this) else Offset.Unspecified


// INT
fun Density.intPxToDp(px: Int) = px.toDp()

fun Density.intPxToSp(px: Int) = px.toSp()

@Composable
fun Int.toDp() = LocalDensity.current.intPxToDp(this)

@Composable
fun Int.toSp() = LocalDensity.current.intPxToSp(this)

fun Int.toFloatPx() = toFloat()

fun Int.toRecIntSize() = IntSize(this, this)

fun Int.toRecIntOffset() = IntOffset(this, this)


// DP SIZE
fun Density.dpSizeToIntSize(dpSize: DpSize) =
    if (dpSize.isSpecified) IntSize(dpSize.width.toPx().toInt(), dpSize.height.toPx().toInt())
    else IntSize.Zero

fun Density.dpSizeRoundToIntSize(dpSize: DpSize) =
    if (dpSize.isSpecified) IntSize(dpSize.width.roundToPx(), dpSize.height.roundToPx())
    else IntSize.Zero

fun Density.dpSizeToSize(dpSize: DpSize) =
    if (dpSize.isSpecified) Size(dpSize.width.toPx(), dpSize.height.toPx())
    else Size.Unspecified

@Composable
fun DpSize.toIntSize() = LocalDensity.current.dpSizeToIntSize(this)

@Composable
fun DpSize.roundToIntSize() = LocalDensity.current.dpSizeRoundToIntSize(this)

@Composable
fun DpSize.toSize() = LocalDensity.current.dpSizeToSize(this)

fun DpSize.isSpaced() = isSpecified && width > 0.dp && height > 0.dp


// SIZE
fun Density.sizeToDpSize(size: Size) =
    if (size.isSpecified) DpSize(size.width.toDp(), size.height.toDp())
    else DpSize.Unspecified

@Composable
fun Size.toDpSize() =
    if (isSpecified) LocalDensity.current.sizeToDpSize(this)
    else DpSize.Unspecified

fun Size.toIntSize() =
    if (isSpecified) IntSize(width.toInt(), height.toInt())
    else IntSize.Zero

fun Size.isSpaced() = isSpecified && width > 0F && height > 0F


// INT SIZE
fun Density.intSizeToDpSize(intSize: IntSize) = DpSize(intSize.width.toDp(), intSize.height.toDp())

@Composable
fun IntSize.toDpSize() = LocalDensity.current.intSizeToDpSize(this)

@Composable
fun IntSize.toSize() = Size(width.toFloat(), height.toFloat())

fun IntSize.isSpaced() = width > 0 && height > 0


// DP OFFSET
fun Density.dpOffsetToIntOffset(dpOffset: DpOffset) =
    if (dpOffset.isSpecified) IntOffset(dpOffset.x.toPx().toInt(), dpOffset.y.toPx().toInt())
    else IntOffset.Zero

fun Density.dpOffsetRoundToIntOffset(dpOffset: DpOffset) =
    if (dpOffset.isSpecified) IntOffset(dpOffset.x.roundToPx(), dpOffset.y.roundToPx())
    else IntOffset.Zero

fun Density.dpOffsetToOffset(dpOffset: DpOffset) =
    if (dpOffset.isSpecified) Offset(dpOffset.x.toPx(), dpOffset.y.toPx())
    else Offset.Unspecified

@Composable
fun DpOffset.toIntOffset() = LocalDensity.current.dpOffsetToIntOffset(this)

@Composable
fun DpOffset.roundToIntOffset() = LocalDensity.current.dpOffsetRoundToIntOffset(this)

@Composable
fun DpOffset.toOffset() = LocalDensity.current.dpOffsetToOffset(this)


// OFFSET
fun Density.offsetToDpOffset(offset: Offset) =
    if (offset.isSpecified) DpOffset(offset.x.toDp(), offset.y.toDp())
    else DpOffset.Unspecified

@Composable
fun Offset.toDpOffset() = LocalDensity.current.offsetToDpOffset(this)

fun Offset.toIntOffset() =
    if (isSpecified) IntOffset(x.toInt(), y.toInt())
    else IntOffset.Zero

// INT OFFSET
fun Density.intOffsetToDpOffset(intOffset: IntOffset) = DpOffset(intOffset.x.toDp(), intOffset.y.toDp())

@Composable
fun IntOffset.toDpOffset() = LocalDensity.current.intOffsetToDpOffset(this)

fun IntOffset.toOffset() = Offset(x.toFloat(), y.toFloat())