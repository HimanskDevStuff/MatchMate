package com.match.matchmate.presentation.model

/**
 * Represents the UI model for a single item in the Matchmate feature.
 * This class is optimized for display in the Presentation Layer.
 */
data class MatchmateUiModel(
    val id: String,
    val title: String,
    val description: String
)