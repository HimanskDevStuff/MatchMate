package com.match.matchmate.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import android.content.Context
import com.match.matchmate.data.local.dao.MatchMateDao
import com.match.matchmate.data.local.entity.MatchMateEntity
import com.match.matchmate.data.model.MatchStatus

@Database(
    entities = [MatchMateEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(MatchStatusConverter::class)
abstract class MatchMateDatabase : RoomDatabase() {

    abstract fun matchMateDao(): MatchMateDao

    companion object {
        @Volatile
        private var INSTANCE: MatchMateDatabase? = null

        fun getDatabase(context: Context): MatchMateDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MatchMateDatabase::class.java,
                    "match_mate_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class MatchStatusConverter {
    @TypeConverter
    fun fromMatchStatus(status: MatchStatus): String {
        return status.name
    }

    @TypeConverter
    fun toMatchStatus(status: String): MatchStatus {
        return MatchStatus.valueOf(status)
    }
}