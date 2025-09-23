package com.match.matchmate.presentation.matchMate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.match.matchmate.domain.usecase.GetMatchmateDataUseCase
import com.match.matchmate.presentation.matchMate.contracts.MatchmateAction
import com.match.matchmate.presentation.matchMate.contracts.MatchmateEvent
import com.match.matchmate.presentation.matchMate.contracts.MatchmateState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Manages the business logic and state for the Matchmate feature.
 */
@HiltViewModel
class MatchmateViewModel @Inject constructor(
    private val getMatchmateDataUseCase: GetMatchmateDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MatchmateState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<MatchmateEvent>()
    val event = _event.asSharedFlow()

    init {
        loadInitialData()
    }

    fun onAction(action: MatchmateAction) {
        when (action) {
            else -> {
            }
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            getMatchmateDataUseCase()
                .onSuccess {
                }
                .onFailure {
                }

            _state.update { it.copy(isLoading = false) }
        }
    }
}