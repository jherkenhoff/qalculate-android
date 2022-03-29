package com.mrkenhoff.qalculate.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.mrkenhoff.libqalculate.Calculator

@HiltViewModel
class MainViewModel @Inject constructor(private val calculator: Calculator) : ViewModel() {


    val inputStringPrivate = MutableLiveData<String>()
    val inputString: LiveData<String> get() = inputStringPrivate

    val resultStringPrivate = MutableLiveData<String>()
    val resultString: LiveData<String> get() = resultStringPrivate

    fun setInput(input: String) {
        inputStringPrivate.value = input

        if (true) { // TODO: If auto-calculation is switched on
            doCalculation()
        }
    }


    private fun doCalculation() {
        resultStringPrivate.value = calculator.calculateAndPrint(inputStringPrivate.value, 2000)
    }
}