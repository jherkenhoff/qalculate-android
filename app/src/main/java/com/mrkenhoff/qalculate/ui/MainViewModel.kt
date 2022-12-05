package com.mrkenhoff.qalculate.ui

import android.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.mrkenhoff.libqalculate.Calculator
import com.mrkenhoff.libqalculate.PrintOptions
import com.mrkenhoff.libqalculate.EvaluationOptions

@HiltViewModel
class MainViewModel @Inject constructor(private val calculator: Calculator, private val po: PrintOptions , private val eo: EvaluationOptions) : ViewModel() {

    val _inputString = MutableLiveData<String>()
    val inputString: LiveData<String> get() = _inputString

    val _resultString = MutableLiveData<String>()
    val resultString: LiveData<String> get() = _resultString

    fun setInput(input: String) {
        _inputString.value = input

        if (true) { // TODO: If auto-calculation is switched on
            doCalculation()
        }
    }

    private fun doCalculation() {
        _resultString.value = calculator.calculateAndPrint(_inputString.value, 2000, eo, po)
    }

    fun getAutoCompletionList(): Array<String> {

        val arr = arrayOf(
            "boltzmann", "planck"
        )
        return arr
    }
}