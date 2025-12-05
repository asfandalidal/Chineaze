package com.azeemi.chineaze.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocab")
data class Vocabulary(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val moduleId: Int,
    val hanzi: String,
    val pinyin: String,
    val english: String,
    val urduScript: String,
    val urduRomanized: String,
    val romanUrdu: String
)