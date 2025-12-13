package com.azeemi.chineaze.ui

import android.speech.tts.TextToSpeech
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.azeemi.chineaze.domain.model.CategoryData
import com.azeemi.chineaze.ui.screens.components.FlashcardScreenWithTts
import com.azeemi.chineaze.ui.screens.components.HomeScreen
import java.util.*

@Composable
fun ChineazeNavHost(viewModel: VocabViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Initialize TTS once
    val tts = remember {
        var ttsInstance: TextToSpeech? = null
        ttsInstance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsInstance?.language = Locale("zh", "CN") // safe reference
            }
        }
        ttsInstance!!
    }

    // Clean up TTS when composable leaves composition
    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    NavHost(
        navController = navController,
        startDestination = "menu"
    ) {
        composable("menu") {
            HomeScreen { categoryId ->
                navController.navigate("flashcards/$categoryId")
            }
        }

        composable("flashcards/{categoryId}") { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull() ?: 0
            val vocabList by viewModel.vocabList.collectAsState()
            val moduleTitle = CategoryData.categories.find { it.id == categoryId }?.title ?: ""

            LaunchedEffect(categoryId) {
                viewModel.loadModule(categoryId)
            }

            FlashcardScreenWithTts(
                vocabList = vocabList,
                moduleId = categoryId,
                moduleTitle = moduleTitle,
                tts = tts,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
