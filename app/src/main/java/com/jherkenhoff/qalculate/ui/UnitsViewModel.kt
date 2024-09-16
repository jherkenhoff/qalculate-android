package com.jherkenhoff.qalculate.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.jherkenhoff.libqalculate.Calculator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UnitsViewModel @Inject constructor(
    private val calc: Calculator,
) : ViewModel() {

    val parsedString : MutableState<String> = mutableStateOf("0")

    val unitNames : List<String> = calc.units.map { it.name() }
}