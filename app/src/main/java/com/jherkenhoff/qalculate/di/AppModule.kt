package com.jherkenhoff.qalculate.di

import android.app.Application
import android.content.Context
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.qalculate.data.ScreenSettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideCalculator(application: Application): Calculator {
        val calc = Calculator()
        calc.loadGlobalDefinitions()
        return calc
    }

    @Provides
    @Singleton
    fun provideScreenSettings(@ApplicationContext context: Context): ScreenSettingsRepository {
        return ScreenSettingsRepository(context)
    }
}