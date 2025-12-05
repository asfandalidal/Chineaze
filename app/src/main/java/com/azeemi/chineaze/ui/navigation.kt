package com.azeemi.chineaze.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.azeemi.chineaze.ui.screens.components.FlashcardScreen
import com.azeemi.chineaze.ui.screens.components.MainMenuScreen

@Composable
fun ChineazeNavHost(viewModel: VocabViewModel) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "menu") {
        composable("menu") { MainMenuScreen { category ->
            navController.navigate("flashcards/$category")
        } }
        composable("flashcards/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            FlashcardScreen(viewModel, category)
        }
    }
}
