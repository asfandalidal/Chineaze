package com.azeemi.chineaze.domain.model

import com.azeemi.chineaze.R

object CategoryData {

    val categories = listOf(
        ChineseCategory(
            id = 1,
            title = "Countries",
            iconRes = R.drawable.ic_countries
        ),
        ChineseCategory(
            id = 2,
            title = "Fruits",
            iconRes = R.drawable.ic_fruits
        ),
        ChineseCategory(
            id = 3,
            title = "Numbers",
            iconRes = R.drawable.ic_numbers
        ),
        ChineseCategory(
            id = 4,
            title = "Languages",
            iconRes = R.drawable.ic_languages
        ),
        ChineseCategory(
            id = 5,
            title = "Animals",
            iconRes = R.drawable.ic_animals
        ),
        ChineseCategory(
            id = 6,
            title = "Body Parts",
            iconRes = R.drawable.ic_body_parts
        )
    )
}


object ModuleMapping {
    const val COUNTRIES = 1
    const val FRUITS = 2
    const val NUMBERS = 3
    const val LANGUAGES = 4
    const val ANIMALS = 5
    const val BODYPARTS = 6

    fun csvFor(moduleId: Int): String? {
        return when (moduleId) {
            COUNTRIES -> "countries-list.csv"
            FRUITS -> "fruits.csv"
            NUMBERS -> "chinese_numbers.csv"
            LANGUAGES -> "languages.csv"
            ANIMALS -> "animals.csv"
            BODYPARTS -> "body_parts.csv"
            else -> null
        }
    }
}


