package com.mrkenhoff.qalculate.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.mrkenhoff.libqalculate.Calculator

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
        return Calculator()
    }
}