package com.azeemi.chineaze.utils

import android.content.Context
import com.azeemi.chineaze.data.repository.VocabRepository
import com.azeemi.chineaze.domain.model.Vocabulary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

suspend fun loadNumbersFromCsv(context: Context, repo: VocabRepository) {
    withContext(Dispatchers.IO) {
        val assetManager = context.assets
        val inputStream = assetManager.open("chinese_numbers.csv") // your file name
        val reader = BufferedReader(InputStreamReader(inputStream))
        val list = mutableListOf<Vocabulary>()
        reader.useLines { lines ->
            lines.drop(1).forEach { line -> // skip header
                val cols = line.split(",")
                if (cols.size >= 6)
                {
                    val vocab = Vocabulary(
                        moduleId = 1, // Numbers module
                        hanzi = cols[0].trim(),
                        pinyin = cols[1].trim(),
                        english = cols[2].trim(),
                        urduScript = cols[3].trim(),
                        urduRomanized = cols[4].trim(),
                        romanUrdu = cols[5].trim()
                    )
                    list.add(vocab)
                }
            }
        }
        repo.insertAll(list)
    }
}
