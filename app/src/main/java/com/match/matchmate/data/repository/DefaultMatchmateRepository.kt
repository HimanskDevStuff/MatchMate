package com.match.matchmate.data.repository

import com.match.matchmate.data.mappers.toDomain
import com.match.matchmate.domain.model.Matchmate
import com.match.matchmate.domain.repository.MatchmateRepository
import javax.inject.Inject

/**
 * Concrete implementation of the repository for the Matchmate feature.
 */
class DefaultMatchmateRepository @Inject constructor() : MatchmateRepository {

    override suspend fun getMatchmateData(): Result<Matchmate> {
        return try {
            val domainModel = Matchmate(id = "1", data = "Sample data from repository")
            Result.success(domainModel)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}