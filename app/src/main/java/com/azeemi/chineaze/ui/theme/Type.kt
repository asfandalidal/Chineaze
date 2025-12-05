package com.azeemi.chineaze.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp


val ChineazeTypography = Typography(

    // Big Hanzi (Chinese characters)
    displayLarge = TextStyle(
        fontSize = 64.sp,
        fontFamily = FontFamily.SansSerif
    ),

    // Medium Hanzi
    displayMedium = TextStyle(
        fontSize = 48.sp,
        fontFamily = FontFamily.SansSerif
    ),

    // Pinyin
    headlineMedium = TextStyle(
        fontSize = 22.sp,
        fontFamily = FontFamily.SansSerif
    ),

    // English / Urdu
    bodyLarge = TextStyle(
        fontSize = 18.sp,
        fontFamily = FontFamily.SansSerif
    ),

    // Roman Urdu / secondary text
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily.SansSerif
    )
)
