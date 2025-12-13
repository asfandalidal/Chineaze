package com.azeemi.chineaze.utils

import android.content.Context
import com.azeemi.chineaze.data.repository.VocabRepository
import com.azeemi.chineaze.domain.model.Vocabulary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

suspend fun loadCsv(
    context: Context,
    repo: VocabRepository,
    csvFile: String,
    moduleId: Int
) {
    withContext(Dispatchers.IO) {
        val inputStream = context.assets.open(csvFile)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val list = mutableListOf<Vocabulary>()

        reader.useLines { lines ->
            lines.drop(1).forEach { line ->
                val cols = line.split(",")

                if (cols.size >= 6) {
                    list.add(
                        Vocabulary(
                            moduleId = moduleId.toString(),
                            hanzi = cols[0].trim(),
                            pinyin = cols[1].trim(),
                            english = cols[2].trim(),
                            urduScript = cols[3].trim(),
                            urduRomanized = cols[4].trim(),
                            romanUrdu = cols[5].trim()
                        )
                    )
                }
            }
        }

        if (list.isNotEmpty()) {
            repo.insertAll(list)
        }
    }
}
