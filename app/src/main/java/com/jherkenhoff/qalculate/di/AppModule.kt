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
}