package com.match.matchmate.presentation.matchMate.contracts

sealed interface MatchmateAction {
   data class LikeClicked(val uuid: String, val index: Int) : MatchmateAction
   data class DislikeClicked(val uuid: String, val index: Int) : MatchmateAction

   data object LoadNextPageData : MatchmateAction
   data object DismissTutorial : MatchmateAction
}