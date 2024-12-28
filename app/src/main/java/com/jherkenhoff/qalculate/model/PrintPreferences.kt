package com.jherkenhoff.qalculate.model

data class PrintPreferences(
    val abbreviateNames: Boolean = true,
) {
    companion object {
        val Default = PrintPreferences()
    }
}

interface PrintPreferencesCallbacks {
    fun onAbbreviateNamesChanged(newValue: Boolean)

    companion object {
        val Default = object: PrintPreferencesCallbacks{
            override fun onAbbreviateNamesChanged(newValue: Boolean) {}
        }
    }
}