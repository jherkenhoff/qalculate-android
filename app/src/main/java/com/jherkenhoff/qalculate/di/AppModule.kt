package com.jherkenhoff.qalculate.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.PrintOptions
import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.IntervalDisplay

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    internal fun provideCalculator(application: Application): Calculator {
        val calc = Calculator()
        calc.loadGlobalDefinitions()
        return calc
    }

    @Singleton
    @Provides
    internal fun providePrintOptions(application: Application): PrintOptions {
        val po = PrintOptions()
        val intDisp = IntervalDisplay.INTERVAL_DISPLAY_SIGNIFICANT_DIGITS
        po.setInterval_display(intDisp)
        return po
    }

    @Singleton
    @Provides
    internal fun provideEvaluationOptions(application: Application): EvaluationOptions {
        return EvaluationOptions()
    }
}