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
                if (internetChecker.isInternetAvailable) {
                    Log.d(TAG, "Internet available - fetching from API")

                    if (page == 0) {
                        Log.d(TAG, "First page with internet - clearing local data")
                        localDataSource.clearAll()
                    }

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
                                Log.d(
                                    TAG,
                                    "Saving ${apiData.results.size} items to local database for page $page"
                                )
                                localDataSource.insertMatchMates(apiData.results.toEntityList(page))

                                emit(BaseUiState.Success(apiData))
                            } else {
                                Log.d(TAG, "No more data from API for page $page")
                                emit(BaseUiState.Success(MatchMateDto(results = mutableListOf())))
                            }
                        }
                        is BaseUiState.Error -> {
                            Log.e(TAG, "API error for page $page, checking local data")
                            emitLocalPageData(this@flow, page, limit)
                        }
                    }
                }
            } else {
                Log.d(TAG, "No internet - checking local data for page $page")
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
        val hasRequestedPageData = localDataSource.hasPageData(requestedPage)

        if (hasRequestedPageData) {
            Log.d(TAG, "Found local data for page $requestedPage")
            val entities = localDataSource.getMatchMatesByPage(requestedPage)
            val localData = MatchMateDto(results = entities.toResultList().toMutableList())
            flowCollector.emit(BaseUiState.Success(localData))
        } else {
            Log.d(TAG, "No local data available for page $requestedPage")
            flowCollector.emit(BaseUiState.Success(MatchMateDto(results = mutableListOf())))
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