package com.azeemi.chineaze.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.azeemi.chineaze.domain.model.Vocabulary
import com.azeemi.chineaze.ui.VocabViewModel

@Composable
fun FlashcardScreen(
    viewModel: VocabViewModel,
    category: String,
) {
    val words by viewModel.words.collectAsState()
    val loading by viewModel.loading.collectAsState()
    var currentWordIndex by remember { mutableStateOf(0) }
    var showTranslation by remember { mutableStateOf(false) }

    LaunchedEffect(category) {
        val moduleId = when (category) {
            "Numbers" -> 1
            "Animals" -> 2
            "Countries" -> 3
            else -> 0
        }
        if (moduleId != 0) {
            viewModel.loadModule(moduleId)
        }
    }

    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading flashcards...", style = MaterialTheme.typography.bodyLarge)
        }
    } else if (words.isNotEmpty()) {
        Flashcard(
            vocab = words[currentWordIndex],
            showTranslation = showTranslation,
            onShowTranslation = { showTranslation = it },
            onNext = {
                showTranslation = false
                currentWordIndex = (currentWordIndex + 1) % words.size
            },
            playAudio = { /* TODO: Implement audio playback */ }
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No flashcards found for this category.", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun Flashcard(
    vocab: Vocabulary,
    showTranslation: Boolean,
    onShowTranslation: (Boolean) -> Unit,
    onNext: () -> Unit,
    playAudio: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = vocab.hanzi,
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = vocab.pinyin,
                style = MaterialTheme.typography.headlineMedium
            )
            if (showTranslation) {
                Spacer(Modifier.height(12.dp))
                Text(text = vocab.english, style = MaterialTheme.typography.bodyLarge)
                Text(text = vocab.urduScript, style = MaterialTheme.typography.bodyLarge)
                Text(text = vocab.romanUrdu, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { playAudio(vocab.hanzi) }) {
                    Text("Play Audio")
                }
                Button(onClick = { onShowTranslation(!showTranslation) }) {
                    Text(if (showTranslation) "Hide" else "Show")
                }
                Button(onClick = onNext) { Text("Next") }
            }
        }
    }
}