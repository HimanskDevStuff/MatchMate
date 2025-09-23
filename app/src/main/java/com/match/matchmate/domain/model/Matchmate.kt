package com.match.matchmate.domain.model

/**
 * Represents the main domain model for the Matchmate feature.
 * This is the "clean" class used within the app (domain, presentation).
 *
 * @property id The unique identifier of the model.
 * @property data An example data field for the model.
 */
data class Matchmate(
    val id: String,
    val data: String
)