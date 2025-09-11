package com.jherkenhoff.qalculate.data

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ThemeSettingsModule {
    @Provides
    @Singleton
    fun provideThemeSettingsRepository(
        @ApplicationContext context: Context
    ): ThemeSettingsRepository = ThemeSettingsRepository(context)
}
