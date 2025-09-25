package com.match.matchmate.data.repository

import com.match.matchmate.data.base.BaseRepository
import com.match.matchmate.data.service.MatchMateApiService
import com.match.matchmate.domain.repository.MatchMateRepository
import saathi.core.service.InternetChecker
import javax.inject.Inject

class MatchMateRepositoryImpl @Inject constructor(
    private val internetChecker: InternetChecker,
    private val apiService: MatchMateApiService
) : MatchMateRepository, BaseRepository(internetChecker) {

    override suspend fun getMatchmateData() = safeApiCall {
       apiService.getAllUsers()
    }
}