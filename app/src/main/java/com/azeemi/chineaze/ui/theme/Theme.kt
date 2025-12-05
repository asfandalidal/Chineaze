package com.azeemi.chineaze.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

// ------------------------------
// COLOR PALETTE
// ------------------------------
private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    secondary = CoralSecondary,
    tertiary = YellowAccent,

    background = OffWhite,
    surface = OffWhite,

    onPrimary = TextLight,
    onSecondary = TextLight,
    onTertiary = TextDark,
    onBackground = TextDark,
    onSurface = TextDark,

    error = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = TealPrimary,
    secondary = CoralSecondary,
    tertiary = YellowAccent,

    background = Color(0xFF1C1C1E),
    surface = Color(0xFF2C2C2E),

    onPrimary = TextLight,
    onSecondary = TextLight,
    onTertiary = TextLight,
    onBackground = TextLight,
    onSurface = TextLight,

    error = ErrorRed
)

// ------------------------------
// SHAPES
// ------------------------------
val ChineazeShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

// ------------------------------
// APP THEME
// ------------------------------
@Composable
fun ChineazeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme =
        when {
            // Dynamic color only for Android 12+
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme)
                    dynamicDarkColorScheme(context)
                else
                    dynamicLightColorScheme(context)
            }

            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ChineazeTypography,
        shapes = ChineazeShapes,
        content = content
    )
}
