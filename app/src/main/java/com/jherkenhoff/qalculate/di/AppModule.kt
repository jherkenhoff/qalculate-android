package com.jherkenhoff.qalculate.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.qalculate.data.UserPreferencesRepository
import com.jherkenhoff.qalculate.data.model.UserPreferencesSerializer
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
    fun provideUserPreferencesRepository(@ApplicationContext context: Context): UserPreferencesRepository {
        val dataStore = DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { context.dataStoreFile("user_prefs.pb") },
        )
        return UserPreferencesRepository(dataStore)
    }
}
