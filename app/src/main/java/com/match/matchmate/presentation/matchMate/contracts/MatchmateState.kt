package com.match.matchmate.presentation.matchMate.contracts

import com.match.matchmate.data.model.MatchMateDto

data class MatchmateState(
    val isLoading: Boolean = false,
    val isInternetAvailable: Boolean = true,
    val matchMateResponse: MatchMateDto = MatchMateDto(),
    val currentPage: Int = 0,
    val hasMorePages: Boolean = true,
    val pageToLoad: Int = 0,
    val showTutorial: Boolean = true
)