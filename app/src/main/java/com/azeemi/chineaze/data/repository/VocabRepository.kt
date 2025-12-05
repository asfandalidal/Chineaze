package com.azeemi.chineaze.data.repository

import com.azeemi.chineaze.data.local.dao.VocabDao
import com.azeemi.chineaze.domain.model.Vocabulary


class VocabRepository(private val dao: VocabDao) {
    suspend fun getAllWords(): List<Vocabulary> = dao.getAll()


    suspend fun getWordsByModule(moduleId: Int): List<Vocabulary> =
        dao.getByModule(moduleId) // implement query in Dao

    suspend fun insertAll(list: List<Vocabulary>) = dao.insertAll(list)
}