package com.match.matchmate.domain.di

import com.match.matchmate.data.repository.MatchMateRepositoryImpl
import com.match.matchmate.data.service.MatchMateApiService
import com.match.matchmate.domain.repository.MatchMateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import saathi.core.service.InternetChecker
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMatchMateRepository(internetChecker: InternetChecker, apiService: MatchMateApiService) : MatchMateRepository {
       return MatchMateRepositoryImpl(
            internetChecker,
            apiService
        )
    }
}