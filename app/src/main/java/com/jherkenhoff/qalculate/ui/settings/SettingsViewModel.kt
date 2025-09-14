package com.jherkenhoff.qalculate.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jherkenhoff.qalculate.data.UserPreferencesRepository
import com.jherkenhoff.qalculate.model.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

interface UserPreferencesCallbacks {
    fun onSyncUnitsChanged(newValue: Boolean) {}
    fun onAbbreviateNamesChanged(newValue: Boolean) {}
    fun onNegativeExponentsChanged(newValue: Boolean) {}
    fun onSpaciousChanged(newValue: Boolean) {}
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val userPreferences = userPreferencesRepository.userPreferencesFlow.stateIn(
        viewModelScope,
        WhileSubscribed(5000),
        UserPreferences()
    )

    fun updateUserPreferences(userPreferences: UserPreferences) {
        viewModelScope.launch {
            userPreferencesRepository.updateUserPreferences(userPreferences)
        }
    }
}