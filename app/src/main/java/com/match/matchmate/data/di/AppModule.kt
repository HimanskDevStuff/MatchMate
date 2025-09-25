package com.match.matchmate.data.di

import android.app.Application
import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import saathi.core.service.InternetChecker
import saathi.core.service.InternetCheckerImpl
import javax.inject.Singleton

/**
 * Hilt module that provides data layer dependencies for the Matchmate feature.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideChuckerInterceptor(@ApplicationContext appContext: Context): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context = appContext).build()
    }

    @Provides
    @Singleton
    fun provideInternetChecker(@ApplicationContext appContext: Context): InternetChecker {
        return InternetCheckerImpl(appContext)
    }
}