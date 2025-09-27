package com.match.matchmate.presentation.matchMate.contracts

/**
 * Defines the actions that can be sent from the UI to the ViewModel
 * for the Matchmate feature.
 */
sealed interface MatchmateAction {
   data class LikeClicked(val uuid: String, val index: Int) : MatchmateAction
   data class DislikeClicked(val uuid: String, val index: Int) : MatchmateAction

   data object LoadNextPageData : MatchmateAction
   data object RefreshData : MatchmateAction
}