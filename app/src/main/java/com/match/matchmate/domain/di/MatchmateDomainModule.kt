package com.match.matchmate.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import com.match.matchmate.domain.repository.MatchmateRepository
import com.match.matchmate.domain.usecase.GetMatchmateDataUseCase

/**
 * Hilt module that provides domain layer dependencies (use cases) for the Matchmate feature.
 */
@Module
@InstallIn(ViewModelComponent::class)
object MatchmateDomainModule {

    /**
     * Provides the GetMatchmateDataUseCase instance.
     */
    @Provides
    @ViewModelScoped
    fun provideGetMatchmateDataUseCase(
        repository: MatchmateRepository
    ): GetMatchmateDataUseCase {
        return GetMatchmateDataUseCase(repository)
    }
}