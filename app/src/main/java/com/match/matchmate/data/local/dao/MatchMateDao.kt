package com.match.matchmate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.match.matchmate.data.local.entity.MatchMateEntity
import com.match.matchmate.data.model.MatchStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchMateDao {

    @Query("SELECT * FROM match_mate_results ORDER BY created_at ASC")
    fun getAllMatchMates(): Flow<List<MatchMateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatchMates(matchMates: List<MatchMateEntity>)

    @Query("UPDATE match_mate_results SET match_status = :status WHERE uuid = :uuid")
    suspend fun updateMatchStatus(uuid: String, status: MatchStatus)

    @Query("DELETE FROM match_mate_results")
    suspend fun clearAll()

    @Query("SELECT * FROM match_mate_results WHERE page_number = :pageNumber ORDER BY created_at ASC")
    suspend fun getMatchMatesByPage(pageNumber: Int): List<MatchMateEntity>

    @Query("SELECT COUNT(*) FROM match_mate_results WHERE page_number = :pageNumber")
    suspend fun getPageItemCount(pageNumber: Int): Int

}