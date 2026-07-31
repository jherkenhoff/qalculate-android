package com.jherkenhoff.qalculate

import android.app.Application
import android.system.Os
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.net.URL

@HiltAndroidApp
class QalculateApplication : Application() {
    private val applicationScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Default
    )

    override fun onCreate() {
        super.onCreate()

        Os.setenv("HOME", filesDir.absolutePath, true)

        // TODO: Move library loading to libqalculate library
        System.loadLibrary("qalculate_swig")
    }

}

