package com.jherkenhoff.qalculate.ui.units

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.jherkenhoff.libqalculate.Calculator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UnitsViewModel @Inject constructor(
    private val calc: Calculator,
) : ViewModel() {

    private val _searchString = mutableStateOf("")
    val searchString = _searchString

    private val _unitList = mutableStateOf<List<UnitDefinition>>(emptyList())
    val unitList = _unitList

    init {
        updateUnitList()
    }

    fun setSearchString(newSearchString: String) {
        _searchString.value = newSearchString
        updateUnitList()
    }

    private fun updateUnitList() {
        val sorted = calc.units.sortedWith(compareBy { it.title() })

        var filtered = sorted

        if (_searchString.value.isNotEmpty()) {
            filtered = filtered.filter { it.title().lowercase().startsWith(_searchString.value.lowercase()) }
        }

        _unitList.value = filtered.map { UnitDefinition(it.title(), it.name(), it.abbreviation()) }
    }
}