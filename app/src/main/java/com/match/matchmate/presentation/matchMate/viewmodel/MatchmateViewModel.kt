package com.match.matchmate.presentation.matchMate.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.match.matchmate.data.base.BaseUiState
import com.match.matchmate.data.model.MatchMateDto
import com.match.matchmate.data.model.MatchStatus
import com.match.matchmate.domain.usecase.GetMatchmateDataUseCase
import com.match.matchmate.presentation.matchMate.contracts.MatchmateAction
import com.match.matchmate.presentation.matchMate.contracts.MatchmateEvent
import com.match.matchmate.presentation.matchMate.contracts.MatchmateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import saathi.core.service.InternetChecker
import javax.inject.Inject

/**
 * Manages the business logic and state for the Matchmate feature.
 */
@HiltViewModel
class MatchmateViewModel @Inject constructor(
    private val getMatchmateDataUseCase: GetMatchmateDataUseCase,
    private val internetChecker: InternetChecker
) : ViewModel() {

    private val _state = MutableStateFlow(MatchmateState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<MatchmateEvent>()
    val event = _event.asSharedFlow()

    init {
        loadMatchMateDate()
        viewModelScope.launch {
            internetChecker.isNetworkConnectedFlow.collectLatest { isAvailable ->

                _state.update { it.copy(isInternetAvailable = isAvailable) }
            }
        }
    }

    fun onAction(action: MatchmateAction) {
        when (action) {
            is MatchmateAction.LikeClicked -> {
                val updatedResults =
                    _state.value.matchMateResponse.results.mapIndexed { index, result ->
                        if (index == action.index) result.copy(matchStatus = MatchStatus.LIKED)
                        else result
                    }
                _state.update { myState ->
                    myState.copy(
                        matchMateResponse = myState.matchMateResponse.copy(
                            results = updatedResults.toMutableList()
                        )
                    )
                }
            }

            is MatchmateAction.DislikeClicked -> {
                val updatedResults =
                    _state.value.matchMateResponse.results.mapIndexed { index, result ->
                        if (index == action.index) result.copy(matchStatus = MatchStatus.DISLIKED)
                        else result
                    }
                _state.update { myState ->
                    myState.copy(
                        matchMateResponse = myState.matchMateResponse.copy(
                            results = updatedResults.toMutableList()
                        )
                    )
                }
            }

            is MatchmateAction.LoadNextPageData -> {
                // Don't increment page here - do it in loadMatchMateDate after successful response
                if (!_state.value.isLoading && _state.value.hasMorePages) {
                    loadMatchMateDate()
                }
            }

            is MatchmateAction.RefreshData -> {
                _state.update {
                    it.copy(
                        currentPage = 0,
                        hasMorePages = true,
                        matchMateResponse = MatchMateDto()
                    )
                }
                loadMatchMateDate()
            }

            else -> {

            }
        }
    }

    private fun loadMatchMateDate() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Use currentPage + 1 for the API call since most APIs expect 1-based indexing
            val pageToLoad = _state.value.currentPage + 1
            getMatchmateDataUseCase.getMatchMateData(pageToLoad, 10).collectLatest { response ->
                when (response) {
                    is BaseUiState.Loading -> {
                        _state.update { it.copy(
                            isLoading = _state.value.matchMateResponse.results.isEmpty()
                        ) }
                    }

                    is BaseUiState.Success -> {
                        val newResults = response.data?.results ?: emptyList()
                        _state.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                matchMateResponse = currentState.matchMateResponse.copy(
                                    results = ArrayList(currentState.matchMateResponse.results + newResults)
                                ),
                                hasMorePages = newResults.size == 10,
                                // Only increment page after successful response
                                currentPage = if (newResults.isNotEmpty()) currentState.currentPage + 1 else currentState.currentPage
                            )
                        }
                    }

                    is BaseUiState.Error -> {
                        _state.update { it.copy(isLoading = false) }
                        Log.e(
                            "MatchmateViewModel",
                            "Error loading page $pageToLoad: ${response.error.message ?: response.error.code}"
                        )
                    }
                }
            }
        }
    }
}