package com.jherkenhoff.qalculate

import dagger.hilt.android.HiltAndroidApp
import android.app.Application
import com.jherkenhoff.libqalculate.Calculator

@HiltAndroidApp
class QalculateApplication : Application() {
    companion object {
        init {
            // TODO: Move library loading to libqalculate library
            // TODO: Check why this is necessary on some phones... (It should only need libqalculate_swig)
            //  (Best: Link dependencies statically into libqalculate)

            System.loadLibrary("qalculate_swig")
        }
    }
}

