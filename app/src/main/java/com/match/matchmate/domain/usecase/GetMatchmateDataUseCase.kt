package com.match.matchmate.domain.usecase

import com.match.matchmate.domain.model.Matchmate
import com.match.matchmate.domain.repository.MatchmateRepository
import javax.inject.Inject

/**
 * Use case that encapsulates the business logic for fetching the Matchmate feature data.
 */
class GetMatchmateDataUseCase @Inject constructor(
    private val repository: MatchmateRepository
) {

    /**
     * Executes the use case.
     */
    suspend operator fun invoke(): Result<Matchmate> {
        return repository.getMatchmateData()
    }
}