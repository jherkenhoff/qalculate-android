package com.jherkenhoff.qalculate.ui.settings

import androidx.lifecycle.ViewModel
import com.jherkenhoff.qalculate.data.PrintPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val printPreferencesRepository: PrintPreferencesRepository
) : ViewModel() {

}