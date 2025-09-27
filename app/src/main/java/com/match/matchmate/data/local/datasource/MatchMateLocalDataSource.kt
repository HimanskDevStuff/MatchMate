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

    suspend fun getMatchMatesByPage(pageNumber: Int): List<MatchMateEntity> {
        return matchMateDao.getMatchMatesByPage(pageNumber)
    }

    suspend fun getPageItemCount(pageNumber: Int): Int {
        return matchMateDao.getPageItemCount(pageNumber)
    }

    suspend fun hasPageData(pageNumber: Int): Boolean {
        return getPageItemCount(pageNumber) > 0
    }

    suspend fun insertMatchMates(matchMates: List<MatchMateEntity>) {
        matchMateDao.insertMatchMates(matchMates)
    }

    suspend fun updateMatchStatus(uuid: String, status: MatchStatus) {
        matchMateDao.updateMatchStatus(uuid, status)
    }

    suspend fun clearAll() {
        matchMateDao.clearAll()
    }


}