package com.match.matchmate.data.di

import com.match.matchmate.data.repository.DefaultMatchMateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.match.matchmate.domain.repository.MatchmateRepository
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Hilt module that provides data layer dependencies for the Matchmate feature.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    /**
     * Binds the repository implementation to its interface.
     */
    @Provides
    @Singleton
    fun provideRetrofitInstance() = Retrofit.Builder()
}