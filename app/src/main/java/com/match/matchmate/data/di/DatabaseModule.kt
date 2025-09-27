package com.match.matchmate.data.di

import android.content.Context
import androidx.room.Room
import com.match.matchmate.data.local.dao.MatchMateDao
import com.match.matchmate.data.local.database.MatchMateDatabase
import com.match.matchmate.data.local.datasource.MatchMateLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMatchMateDatabase(@ApplicationContext context: Context): MatchMateDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MatchMateDatabase::class.java,
            "match_mate_database"
        ).build()
    }

    @Provides
    fun provideMatchMateDao(database: MatchMateDatabase): MatchMateDao {
        return database.matchMateDao()
    }

    @Provides
    @Singleton
    fun provideMatchMateLocalDataSource(matchMateDao: MatchMateDao): MatchMateLocalDataSource {
        return MatchMateLocalDataSource(matchMateDao)
    }
}