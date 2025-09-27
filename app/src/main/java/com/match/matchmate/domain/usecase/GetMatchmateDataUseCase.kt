package com.match.matchmate.domain.usecase

import com.match.matchmate.data.base.BaseUiState
import com.match.matchmate.data.model.MatchMateDto
import com.match.matchmate.data.model.MatchStatus
import com.match.matchmate.domain.repository.MatchMateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMatchmateDataUseCase @Inject constructor(
    private val repository: MatchMateRepository
) {

    suspend fun getMatchMateData(page: Int, limit: Int): Flow<BaseUiState<MatchMateDto?>> {
        return repository.getMatchmateData(page, limit)
    }

    suspend fun updateMatchStatus(uuid: String, status: MatchStatus) {
        repository.updateMatchStatus(uuid, status)
    }

    suspend fun clearAllData() {
        repository.clearAllData()
    }
}