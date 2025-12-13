package com.azeemi.chineaze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.azeemi.chineaze.data.local.db.AppDatabase
import com.azeemi.chineaze.data.repository.VocabRepository
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

        // Load CSVs once at app start
        lifecycleScope.launch {
            loadCsv(this@MainActivity, repo, "countries-list.csv", 1)
            loadCsv(this@MainActivity, repo, "chinese_numbers.csv", 3)
            loadCsv(this@MainActivity, repo, "languages.csv", 4)
        }

        val viewModel = VocabViewModel(repo)

        setContent {
            ChineazeTheme {
                ChineazeNavHost(viewModel)
            }
        }
    }
}
