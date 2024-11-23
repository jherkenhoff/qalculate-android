package com.jherkenhoff.qalculate

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QalculateApplication : Application() {

    companion object {
        init {
            // TODO: Move library loading to libqalculate library

            System.loadLibrary("qalculate_swig")
        }
    }
}

