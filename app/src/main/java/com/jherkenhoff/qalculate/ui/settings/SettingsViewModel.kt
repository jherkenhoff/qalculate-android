package com.jherkenhoff.qalculate.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jherkenhoff.qalculate.data.PrintPreferencesRepository
import com.jherkenhoff.qalculate.model.PrintPreferencesCallbacks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val printPreferencesRepository: PrintPreferencesRepository
) : ViewModel() {

    val printPreferences = printPreferencesRepository.printPreferencesFlow

    val printPreferencesCallbacks = object: PrintPreferencesCallbacks {
        override fun onAbbreviateNamesChanged(newValue: Boolean) {
            viewModelScope.launch {
                printPreferencesRepository.setAbbreviateNames(newValue)
            }
        }
    }
}