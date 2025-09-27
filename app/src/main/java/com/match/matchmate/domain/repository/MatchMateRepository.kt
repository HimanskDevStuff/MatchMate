package com.match.matchmate.domain.repository

import com.match.matchmate.data.base.BaseUiState
import com.match.matchmate.data.model.MatchMateDto
import com.match.matchmate.data.model.MatchStatus
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for the Matchmate feature's repository.
 */
interface MatchMateRepository {

    /**
     * Retrieves data for the Matchmate feature.
     *
     * @return A Result object containing the Matchmate domain model on success,
     * or an exception on failure.
     */
    suspend fun getMatchmateData(page: Int, limit: Int): Flow<BaseUiState<MatchMateDto?>>

    /**
     * Updates the match status for a specific user
     */
    suspend fun updateMatchStatus(uuid: String, status: MatchStatus)

    /**
     * Clears all local data
     */
    suspend fun clearAllData()
}