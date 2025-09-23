package com.match.matchmate.data.mappers

import com.match.matchmate.data.model.MatchmateDto
import com.match.matchmate.domain.model.Matchmate

/**
 * Maps a MatchmateDto (Data Layer) object to a Matchmate (Domain Layer) object.
 *
 * @return The mapped Matchmate object.
 */
fun MatchmateDto.toDomain(): Matchmate {
    return Matchmate(
        id = this.uniqueId,
        data = this.payload ?: "Data not available"
    )
}