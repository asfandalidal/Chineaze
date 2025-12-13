package com.azeemi.chineaze.ui.screens.components

import android.speech.tts.TextToSpeech
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azeemi.chineaze.domain.model.Vocabulary
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreenWithTts(
    vocabList: List<Vocabulary>,
    moduleId: Int,
    moduleTitle: String,
    tts: TextToSpeech,
    onBackClick: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var streak by remember { mutableStateOf(0) }

    val moduleColor = when (moduleId) {
        1 -> Color(0xFFDC143C)
        2 -> Color(0xFF4CAF50)
        3 -> Color(0xFF2196F3)
        else -> Color(0xFFFFC107)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(moduleTitle, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("${currentIndex + 1} / ${vocabList.size}  |  Streak: $streak",
                            fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = moduleColor)
            )
        }
    ) { padding ->
        if (vocabList.isEmpty()) {
            EmptyFlashcardMessage(padding)
        } else {
            FlashcardContentWithTts(
                vocabList = vocabList,
                currentIndex = currentIndex,
                isFlipped = isFlipped,
                onFlip = { isFlipped = !isFlipped },
                onPrev = {
                    if (currentIndex > 0) {
                        currentIndex--
                        isFlipped = false
                        streak = 0
                    }
                },
                onNext = {
                    if (currentIndex < vocabList.size - 1) {
                        currentIndex++
                        isFlipped = false
                        streak++
                    }
                },
                moduleColor = moduleColor,
                padding = padding,
                tts = tts
            )
        }
    }
}

@Composable
private fun FlashcardContentWithTts(
    vocabList: List<Vocabulary>,
    currentIndex: Int,
    isFlipped: Boolean,
    onFlip: () -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    moduleColor: Color,
    padding: PaddingValues,
    tts: TextToSpeech
) {
    val currentWord = vocabList[currentIndex]

    Column(
        modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LinearProgressIndicator(
            progress = (currentIndex + 1).toFloat() / vocabList.size,
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
            color = moduleColor,
            trackColor = Color.LightGray.copy(alpha = 0.3f)
        )

        Spacer(Modifier.height(16.dp))

        val rotation by animateFloatAsState(
            targetValue = if (isFlipped) 180f else 0f,
            animationSpec = tween(400)
        )

        Card(
            modifier = Modifier.fillMaxWidth().weight(1f)
                .graphicsLayer { rotationY = rotation }
                .clickable(onClick = onFlip),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = moduleColor),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    if (!isFlipped) {
                        // Front side: Chinese Hanzi + Pinyin
                        Text(currentWord.hanzi, fontSize = 42.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(Modifier.height(8.dp))
                        Text(currentWord.pinyin, fontSize = 22.sp, color = Color.White.copy(alpha = 0.9f))
                    } else {
                        // Back side: Urdu + English
                        Text(currentWord.urduScript, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(8.dp))
                        Text(currentWord.english, fontSize = 18.sp, color = Color.White.copy(alpha = 0.9f), textAlign = TextAlign.Center)
                    }

                    Spacer(Modifier.height(16.dp))

                    // Speaker icon: play pronunciation on click
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Play Audio",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp).clickable {
                            tts.language = Locale("zh", "CN")
                            tts.speak(currentWord.pinyin, TextToSpeech.QUEUE_FLUSH, null, null)
                            tts.language = Locale("ur")
                            tts.speak(currentWord.urduRomanized, TextToSpeech.QUEUE_ADD, null, null)
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = if (isFlipped) "پلٹانے کے لیے ٹیپ کریں" else "Tap card to flip",
            fontSize = 18.sp, color = Color.White, textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationButton(enabled = currentIndex > 0, onClick = onPrev, icon = Icons.Default.ArrowBack)
            Text("${currentIndex + 1} / ${vocabList.size}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = moduleColor)
            NavigationButton(enabled = currentIndex < vocabList.size - 1, onClick = onNext, icon = Icons.Default.ArrowForward)
        }
    }
}

@Composable
private fun NavigationButton(enabled: Boolean, onClick: () -> Unit, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(64.dp).background(if (enabled) Color.Red else Color.LightGray, shape = CircleShape)
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
    }
}

@Composable
private fun EmptyFlashcardMessage(padding: PaddingValues) {
    Box(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("😔", fontSize = 64.sp)
            Spacer(Modifier.height(16.dp))
            Text("No vocabulary available", fontSize = 18.sp, color = Color.White)
            Text("اس کیٹیگری میں ابھی کوئی الفاظ نہیں ہیں", fontSize = 14.sp, color = Color.Gray)
        }
    }
}
