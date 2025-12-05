package com.azeemi.chineaze.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.azeemi.chineaze.domain.model.Vocabulary

@Dao
interface VocabDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(words: List<Vocabulary>)

    @Query("SELECT * FROM vocab")
    suspend fun getAll(): List<Vocabulary>

    @Query("SELECT * FROM vocab WHERE moduleId = :moduleId")
    suspend fun getByModule(moduleId: Int): List<Vocabulary>

}