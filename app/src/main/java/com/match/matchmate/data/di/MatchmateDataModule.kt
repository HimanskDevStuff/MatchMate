package com.match.matchmate.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.match.matchmate.data.repository.DefaultMatchmateRepository
import com.match.matchmate.domain.repository.MatchmateRepository
import javax.inject.Singleton

/**
 * Hilt module that provides data layer dependencies for the Matchmate feature.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class MatchmateDataModule {

    /**
     * Binds the repository implementation to its interface.
     */
    @Binds
    @Singleton
    abstract fun bindMatchmateRepository(
        repository: DefaultMatchmateRepository
    ): MatchmateRepository
}