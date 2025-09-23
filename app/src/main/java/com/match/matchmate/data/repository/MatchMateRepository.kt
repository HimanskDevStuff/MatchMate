package com.match.matchmate.data.repository

import com.match.matchmate.data.base.BaseRepository
import com.match.matchmate.domain.model.Matchmate
import com.match.matchmate.domain.repository.MatchmateRepository
import saathi.core.service.InternetChecker
import javax.inject.Inject

class MatchMateRepository @Inject constructor(
    private val internetChecker: InternetChecker
) : MatchmateRepository, BaseRepository(internetChecker) {

    override suspend fun getMatchmateData() = safeApiCall {

    }
}