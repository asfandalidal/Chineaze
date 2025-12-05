package com.azeemi.chineaze.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.azeemi.chineaze.data.local.dao.VocabDao
import com.azeemi.chineaze.domain.model.Vocabulary

@Database(entities = [Vocabulary::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vocabDao(): VocabDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chineaze_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}