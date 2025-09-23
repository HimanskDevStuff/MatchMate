package com.match.matchmate.presentation.matchMate.contracts

/**
 * Defines the actions that can be sent from the UI to the ViewModel
 * for the Matchmate feature.
 */
sealed interface MatchmateAction {
   data class ButtonClicked(val itemId: String) : MatchmateAction
}