package com.match.matchmate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.match.matchmate.domain.usecase.GetMatchmateDataUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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