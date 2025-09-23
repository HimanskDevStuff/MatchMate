package com.match.matchmate.domain.repository

import com.match.matchmate.domain.model.Matchmate

/**
 * Interface defining the contract for the Matchmate feature's repository.
 */
interface MatchmateRepository {

    /**
     * Retrieves data for the Matchmate feature.
     *
     * @return A Result object containing the Matchmate domain model on success,
     * or an exception on failure.
     */
    suspend fun getMatchmateData(): Result<Matchmate>
}