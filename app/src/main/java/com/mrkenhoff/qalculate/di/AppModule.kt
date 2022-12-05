package com.mrkenhoff.qalculate.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.mrkenhoff.libqalculate.Calculator
import com.mrkenhoff.libqalculate.PrintOptions
import com.mrkenhoff.libqalculate.EvaluationOptions
import com.mrkenhoff.libqalculate.IntervalDisplay

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    internal fun provideCalculator(application: Application): Calculator {
        // TODO: Is this a good place to load libraries?
        // TODO: Check why this is necessary on some phones... (It should only need libqalculate_swig)
        //  (Best: Link dependencies statically into libqalculate)
        System.loadLibrary("c++_shared")
        System.loadLibrary("lzma")
        System.loadLibrary("gmp")
        System.loadLibrary("mpfr")
        System.loadLibrary("iconv")
        System.loadLibrary("xml2")
        System.loadLibrary("qalculate")
        System.loadLibrary("libqalculate_swig")
        var calc = Calculator()
        calc.loadGlobalDefinitions()
        return calc
    }

    @Singleton
    @Provides
    internal fun providePrintOptions(application: Application): PrintOptions {
        var po = PrintOptions()

        var intDisp = IntervalDisplay.INTERVAL_DISPLAY_SIGNIFICANT_DIGITS
        po.setInterval_display(intDisp)
        return po
    }

    @Singleton
    @Provides
    internal fun provideEvaluationOptions(application: Application): EvaluationOptions {
        var eo = EvaluationOptions()
        return eo
    }
}