package com.match.matchmate.domain.repository

import com.match.matchmate.data.base.BaseUiState
import com.match.matchmate.data.model.MatchMateDto
import com.match.matchmate.data.model.MatchStatus
import kotlinx.coroutines.flow.Flow

interface MatchMateRepository {

    suspend fun getMatchmateData(page: Int, limit: Int): Flow<BaseUiState<MatchMateDto?>>

    suspend fun updateMatchStatus(uuid: String, status: MatchStatus)

    suspend fun clearAllData()
}