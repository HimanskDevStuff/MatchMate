package com.match.matchmate.domain.usecase

import com.match.matchmate.data.base.BaseUiState
import com.match.matchmate.data.model.MatchMateDto
import com.match.matchmate.domain.repository.MatchMateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case that encapsulates the business logic for fetching the Matchmate feature data.
 */
class GetMatchmateDataUseCase @Inject constructor(
    private val repository: MatchMateRepository
) {

    /**
     * Executes the use case.
     */
    suspend fun getMatchMateData(page: Int, limit: Int): Flow<BaseUiState<MatchMateDto?>> {
        return repository.getMatchmateData(page, limit)
    }
}