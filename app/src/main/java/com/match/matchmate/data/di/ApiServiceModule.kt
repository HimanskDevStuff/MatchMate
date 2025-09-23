package com.match.matchmate.data.di

import com.match.matchmate.data.service.MatchMateApiService
import com.match.matchmate.domain.model.Matchmate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) {
        retrofit.create(MatchMateApiService::class.java)
    }
}