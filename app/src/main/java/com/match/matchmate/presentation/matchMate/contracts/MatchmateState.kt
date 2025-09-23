package com.match.matchmate.presentation.matchMate.contracts

/**
 * Represents the state of the Matchmate screen.
 *
 * @property isLoading True if data is currently being loaded.
 * @property items The list of UI models to be displayed.
 */
data class MatchmateState(
    val isLoading: Boolean = false,
    val items: List<MatchmateUiModel> = emptyList()
)