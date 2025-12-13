package com.azeemi.chineaze.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.azeemi.chineaze.domain.model.Vocabulary
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabDao {

    @Query("SELECT * FROM vocab WHERE moduleId = :moduleId ORDER BY id ASC")
    fun getVocabByModule(moduleId: String): Flow<List<Vocabulary>>

    @Query("SELECT * FROM vocab")
    fun getAllVocab(): Flow<List<Vocabulary>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(words: List<Vocabulary>)

    @Query("SELECT COUNT(*) FROM vocab")
    suspend fun getVocabCount(): Int

    @Query("DELETE FROM vocab")
    suspend fun deleteAll()

    @Query("SELECT DISTINCT moduleId FROM vocab")
    suspend fun getAllModules(): List<String>

    @Query("SELECT COUNT(*) FROM vocab WHERE moduleId = :moduleId")
    suspend fun getModuleCount(moduleId: String): Int

}

