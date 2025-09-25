package com.match.matchmate.presentation.matchMate.contracts

import com.match.matchmate.data.model.MatchMateDto

/**
 * Represents the state of the Matchmate screen.
 *
 * @property isLoading True if data is currently being loaded.
 * @property items The list of UI models to be displayed.
 */
data class MatchmateState(
    val isLoading: Boolean = false,
    val matchMateResponse: MatchMateDto = MatchMateDto()
)