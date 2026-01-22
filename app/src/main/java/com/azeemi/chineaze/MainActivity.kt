package com.azeemi.chineaze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.azeemi.chineaze.data.local.db.AppDatabase
import com.azeemi.chineaze.data.repository.VocabRepository
import com.azeemi.chineaze.domain.model.CategoryData
import com.azeemi.chineaze.domain.model.ModuleMapping
import com.azeemi.chineaze.ui.ChineazeNavHost
import com.azeemi.chineaze.ui.VocabViewModel
import com.azeemi.chineaze.ui.theme.ChineazeTheme
import com.azeemi.chineaze.utils.loadCsv
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(this)
        val repo = VocabRepository(db.vocabDao())
        val viewModel = VocabViewModel(repo)

        // Scalable Sync: Loops through all defined categories
        lifecycleScope.launch {
            syncAllModules(repo)
        }

        setContent {
            ChineazeTheme {
                ChineazeNavHost(viewModel)
            }
        }
    }

    private suspend fun syncAllModules(repo: VocabRepository) {
        // Iterate through all categories defined in your CategoryData
        CategoryData.categories.forEach { category ->
            val fileName = ModuleMapping.csvFor(category.id)
            if (fileName != null) {
                // The repository handles the "if empty" check
                repo.checkAndLoadAssets(this, category.id)
            }
        }
    }
}
