package com.azeemi.chineaze.data.repository

import android.content.Context
import com.azeemi.chineaze.data.local.dao.VocabDao
import com.azeemi.chineaze.domain.model.ModuleMapping
import com.azeemi.chineaze.domain.model.Vocabulary


class VocabRepository(private val dao: VocabDao) {

    fun getWordsByModule(moduleId: Int) =
        dao.getVocabByModule(moduleId.toString())

    suspend fun insertAll(list: List<Vocabulary>) =
        dao.insertAll(list)

    suspend fun getModuleCount(moduleId: Int): Int =
        dao.getModuleCount(moduleId.toString())

    suspend fun checkAndLoadAssets(context: Context, moduleId: Int) {
        val count = getModuleCount(moduleId)

        // Efficiency: Only parse CSV if the database table for this module is empty
        if (count == 0) {
            val fileName = ModuleMapping.csvFor(moduleId) ?: return
            try {
                val words = parseCsv(context, fileName, moduleId)
                insertAll(words)
            } catch (e: Exception) {
                e.printStackTrace() // Handle file not found or parsing errors
            }
        }
    }

    private fun parseCsv(context: Context, fileName: String, moduleId: Int): List<Vocabulary> {
        val list = mutableListOf<Vocabulary>()
        context.assets.open(fileName).bufferedReader().useLines { lines ->
            lines.drop(1).forEach { line ->
                val tokens = line.split(",")
                if (tokens.size >= 6) {
                    list.add(Vocabulary(
                        moduleId = moduleId.toString(),
                        hanzi = tokens[0].trim(),
                        pinyin = tokens[1].trim(),
                        english = tokens[2].trim(),
                        urduScript = tokens[3].trim(),
                        urduRomanized = tokens[4].trim(),
                        romanUrdu = tokens[5].trim()
                    ))
                }
            }
        }
        return list
    }
}


