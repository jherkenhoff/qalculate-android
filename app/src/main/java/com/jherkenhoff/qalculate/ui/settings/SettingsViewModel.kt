package com.jherkenhoff.qalculate.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jherkenhoff.qalculate.data.UserPreferencesRepository
import com.jherkenhoff.qalculate.data.model.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

interface UserPreferencesCallbacks {
    fun onAbbreviateNamesChanged(newValue: Boolean) {}
    fun onNegativeExponentsChanged(newValue: Boolean) {}
    fun onSpaciousChanged(newValue: Boolean) {}
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val userPreferences = userPreferencesRepository.userPreferencesFlow.stateIn(
        viewModelScope,
        WhileSubscribed(5000),
        UserPreferences.getDefaultInstance()
    )

    val userPreferencesCallbacks = object: UserPreferencesCallbacks {
        override fun onAbbreviateNamesChanged(newValue: Boolean) {
            viewModelScope.launch {
                //userPreferencesRepository.setAbbreviateNames(newValue)
            }
        }

        override fun onNegativeExponentsChanged(newValue: Boolean) {
            viewModelScope.launch {
                //printPreferencesRepository.setNegativeExponents(newValue)
            }
        }

        override fun onSpaciousChanged(newValue: Boolean) {
            viewModelScope.launch {
                //printPreferencesRepository.setSpacious(newValue)
            }
        }
    }
}