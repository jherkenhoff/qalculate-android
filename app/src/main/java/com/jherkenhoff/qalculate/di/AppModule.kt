package com.jherkenhoff.qalculate.di

import android.app.Application
import com.jherkenhoff.libqalculate.Calculator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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