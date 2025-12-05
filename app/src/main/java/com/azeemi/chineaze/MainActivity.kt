package com.azeemi.chineaze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.azeemi.chineaze.data.local.db.AppDatabase
import com.azeemi.chineaze.data.repository.VocabRepository
import com.azeemi.chineaze.ui.VocabViewModel
import com.azeemi.chineaze.ui.ChineazeNavHost
import com.azeemi.chineaze.ui.theme.ChineazeTheme
import com.azeemi.chineaze.utils.loadNumbersFromCsv
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = AppDatabase.getDatabase(this)
        val repo = VocabRepository(db.vocabDao())
        val viewModel = VocabViewModel(repo)

        lifecycleScope.launch {
            loadNumbersFromCsv(this@MainActivity, repo)
        }
        setContent {
            ChineazeTheme {
                ChineazeNavHost(viewModel)
            }
        }
    }
}
