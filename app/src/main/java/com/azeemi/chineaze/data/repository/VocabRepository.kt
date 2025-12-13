package com.azeemi.chineaze.data.repository

import com.azeemi.chineaze.data.local.dao.VocabDao
import com.azeemi.chineaze.domain.model.Vocabulary


class VocabRepository(private val dao: VocabDao) {

    fun getWordsByModule(moduleId: Int) =
        dao.getVocabByModule(moduleId.toString())

    suspend fun insertAll(list: List<Vocabulary>) =
        dao.insertAll(list)

    suspend fun getModuleCount(moduleId: Int): Int =
        dao.getModuleCount(moduleId.toString())
}

