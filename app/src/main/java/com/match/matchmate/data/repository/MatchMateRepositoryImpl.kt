package com.match.matchmate.data.repository

import android.util.Log
import com.match.matchmate.data.base.BaseRepository
import com.match.matchmate.data.base.BaseUiState
import com.match.matchmate.data.local.datasource.MatchMateLocalDataSource
import com.match.matchmate.data.mapper.toEntityList
import com.match.matchmate.data.mapper.toResultList
import com.match.matchmate.data.model.MatchMateDto
import com.match.matchmate.data.model.MatchStatus
import com.match.matchmate.data.service.MatchMateApiService
import com.match.matchmate.domain.repository.MatchMateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import saathi.core.service.InternetChecker
import javax.inject.Inject

class MatchMateRepositoryImpl @Inject constructor(
    private val internetChecker: InternetChecker,
    private val apiService: MatchMateApiService,
    private val localDataSource: MatchMateLocalDataSource
) : MatchMateRepository, BaseRepository(internetChecker) {

    companion object {
        private const val TAG = "MatchMateRepository"
    }

    override suspend fun getMatchmateData(page: Int, limit: Int): Flow<BaseUiState<MatchMateDto?>> =
        flow {
            emit(BaseUiState.Loading)

            Log.d(TAG, "Fetching data for page: $page, limit: $limit")

            try {
                // Check if internet is available
                if (internetChecker.isInternetAvailable) {
                    Log.d(TAG, "Internet available - fetching from API")

                    // If it's the first page, clear existing data when internet is available
                    if (page == 0) {
                        Log.d(TAG, "First page with internet - clearing local data")
                        localDataSource.clearAll()
                    }

                // Fetch from API using safeApiCall
                val apiResult = safeApiCall {
                    apiService.getAllUsers(page = page, results = limit)
                }

                apiResult.collect { apiState ->
                    when (apiState) {
                        is BaseUiState.Loading -> {
                            emit(BaseUiState.Loading)
                        }
                        is BaseUiState.Success -> {
                            val apiData = apiState.data
                            if (apiData != null && apiData.results.isNotEmpty()) {
                                // Save to local database with page number
                                Log.d(
                                    TAG,
                                    "Saving ${apiData.results.size} items to local database for page $page"
                                )
                                localDataSource.insertMatchMates(apiData.results.toEntityList(page))

                                // Emit API data
                                emit(BaseUiState.Success(apiData))
                            } else {
                                // No more data from API
                                Log.d(TAG, "No more data from API for page $page")
                                emit(BaseUiState.Success(MatchMateDto(results = mutableListOf())))
                            }
                        }
                        is BaseUiState.Error -> {
                            Log.e(TAG, "API error for page $page, checking local data")
                            // API failed, check if we have this specific page locally
                            emitLocalPageData(this@flow, page, limit)
                        }
                    }
                }
            } else {
                Log.d(TAG, "No internet - checking local data for page $page")
                // No internet, check if we have this specific page locally
                emitLocalPageData(this@flow, page, limit)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Repository error: ${e.message}", e)
            emit(
                BaseUiState.Error(
                    com.match.matchmate.data.base.ErrorResponse(
                        code = "REPOSITORY_ERROR",
                        message = e.message
                    )
                )
            )
        }
    }

    private suspend fun emitLocalPageData(
        flowCollector: FlowCollector<BaseUiState<MatchMateDto?>>,
        requestedPage: Int,
        limit: Int
    ) {
        // Check if we have data for the requested page
        val hasRequestedPageData = localDataSource.hasPageData(requestedPage)

        if (hasRequestedPageData) {
            Log.d(TAG, "Found local data for page $requestedPage")
            // Get only the specific page data
            val entities = localDataSource.getMatchMatesByPage(requestedPage)
            val localData = MatchMateDto(results = entities.toResultList().toMutableList())
            flowCollector.emit(BaseUiState.Success(localData))
        } else {
            Log.d(TAG, "No local data available for page $requestedPage")
            // No data for this page - emit empty result
            flowCollector.emit(BaseUiState.Success(MatchMateDto(results = mutableListOf())))
        }
    }

    private suspend fun emitLocalData(flowCollector: FlowCollector<BaseUiState<MatchMateDto?>>) {
        localDataSource.getAllMatchMates().collect { entities ->
            if (entities.isNotEmpty()) {
                Log.d(TAG, "Emitting ${entities.size} items from local database")
                val localData = MatchMateDto(results = entities.toResultList().toMutableList())
                flowCollector.emit(BaseUiState.Success(localData))
            } else {
                Log.d(TAG, "No local data available")
                flowCollector.emit(BaseUiState.Success(MatchMateDto(results = mutableListOf())))
            }
        }
    }

    override suspend fun updateMatchStatus(uuid: String, status: MatchStatus) {
        try {
            Log.d(TAG, "Updating match status for $uuid to $status")
            localDataSource.updateMatchStatus(uuid, status)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating match status: ${e.message}", e)
        }
    }

    override suspend fun clearAllData() {
        try {
            Log.d(TAG, "Clearing all local data")
            localDataSource.clearAll()
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing data: ${e.message}", e)
        }
    }
}