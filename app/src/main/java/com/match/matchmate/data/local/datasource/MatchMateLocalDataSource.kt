package com.match.matchmate.data.local.datasource

import com.match.matchmate.data.local.dao.MatchMateDao
import com.match.matchmate.data.local.entity.MatchMateEntity
import com.match.matchmate.data.model.MatchStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchMateLocalDataSource @Inject constructor(
    private val matchMateDao: MatchMateDao
) {

    fun getAllMatchMates(): Flow<List<MatchMateEntity>> {
        return matchMateDao.getAllMatchMates()
    }

    suspend fun getMatchMateByUuid(uuid: String): MatchMateEntity? {
        return matchMateDao.getMatchMateByUuid(uuid)
    }

    suspend fun insertMatchMates(matchMates: List<MatchMateEntity>) {
        matchMateDao.insertMatchMates(matchMates)
    }

    suspend fun insertMatchMate(matchMate: MatchMateEntity) {
        matchMateDao.insertMatchMate(matchMate)
    }

    suspend fun updateMatchStatus(uuid: String, status: MatchStatus) {
        matchMateDao.updateMatchStatus(uuid, status)
    }

    suspend fun clearAll() {
        matchMateDao.clearAll()
    }

    suspend fun getCount(): Int {
        return matchMateDao.getCount()
    }

    suspend fun hasData(): Boolean {
        return getCount() > 0
    }
}