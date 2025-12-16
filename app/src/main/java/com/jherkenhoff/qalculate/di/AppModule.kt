package com.jherkenhoff.qalculate.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.qalculate.data.database.QalculateDatabase
import com.jherkenhoff.qalculate.data.database.dao.CalculationHistoryItemDao
import com.jherkenhoff.qalculate.data.repository.CalculationHistoryStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("settings")

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    @Singleton
    fun provideAppScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Provides
    @Singleton
    internal fun provideCalculator(application: Application): Calculator {
        val calc = Calculator()
        calc.loadGlobalDefinitions()
        return calc
    }

    @Provides
    @Singleton
    fun provideUserPreferenceDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): QalculateDatabase =
        Room.databaseBuilder(context, QalculateDatabase::class.java, "data.db")
            .build()

    @Provides
    @Singleton
    fun provideCalculationHistoryItemDao(database: QalculateDatabase): CalculationHistoryItemDao = database.calculationHistoryItemDao()


    @Provides
    @Singleton
    fun provideCalculationHistoryStore(dao: CalculationHistoryItemDao): CalculationHistoryStore {
        return CalculationHistoryStore(dao)
    }

}
