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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azeemi.chineaze.R // Ensure this matches your package name
import com.azeemi.chineaze.domain.model.Vocabulary
import java.util.*


@Composable
fun ModuleCompleteOverlay(
    onRestart: () -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .clickable(enabled = false) {}, // Prevent clicking through to cards
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Your DotLottie implementation
            com.dotlottie.dotlottieandroid.DotLottieAnimation(
                width = 300u,
                height = 300u,
                source = com.dotlottie.dotlottieandroid.DotLottieSource.Url("https://lottiefiles-mobile-templates.s3.amazonaws.com/ar-stickers/swag_sticker_piggy.lottie"),
                autoplay = true,
                loop = true,
                speed = 2f,
                playMode = com.dotlottie.dlplayer.Mode.FORWARD,
                modifier = Modifier.clip(RoundedCornerShape(16.dp))
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Sabaq Mukammal!",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Excellent! You've learned all words.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onRestart,
                modifier = Modifier.fillMaxWidth(0.7f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
            ) {
                Text("Practice Again")
            }

            TextButton(onClick = onBack) {
                Text("Back to Home", color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreenWithTts(
    vocabList: List<Vocabulary>,
    moduleId: Int,
    moduleTitle: String,
    tts: TextToSpeech,
    onBackClick: () -> Unit
) {

    var currentIndex by remember(moduleId) { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var streak by remember(moduleId) { mutableIntStateOf(0) }
    var showSuccess by remember { mutableStateOf(false) }

    val moduleColor = when (moduleId) {
        1 -> Color(0xFFDC143C) // Countries
        2 -> Color(0xFF2E7D32) // Fruits (Darker Green for better readability)
        3 -> Color(0xFF1565C0) // Numbers
        4 -> Color(0xFF7B1FA2) // Languages
        5 -> Color(0xFF5D4037) // Animals (Brown)
        6 -> Color(0xFF00796B) // Body Parts (Teal)
        else -> Color(0xFFF9A825)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(moduleTitle, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        // Safety check: Don't show progress if list is empty
                        val total = if (vocabList.isNotEmpty()) vocabList.size else 0
                        Text("${currentIndex + 1} / $total  |  Streak: $streak",
                            fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = moduleColor,
                    titleContentColor = Color.White
                )
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
                        // streak = 0 // Optional: Reset streak if they go back
                    }
                },
                onNext = {
                    if (currentIndex < vocabList.size - 1) {
                        currentIndex++
                        isFlipped = false
                        streak++ // Only increments correctly when moving forward
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
    // Move Font definition here to avoid context issues but keep it accessible
    val urduFont = try {
        FontFamily(Font(R.font.jameelnastaleeq))
    } catch (e: Exception) {
        FontFamily.Default
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / vocabList.size },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = moduleColor,
            trackColor = Color.LightGray.copy(alpha = 0.3f)
        )

        Spacer(Modifier.height(24.dp))

        val rotation by animateFloatAsState(
            targetValue = if (isFlipped) 180f else 0f,
            animationSpec = tween(400),
            label = "CardRotation"
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12 * density
                }
                .clickable(onClick = onFlip),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = moduleColor),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (rotation <= 90f) {
                    /* FRONT SIDE */
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(currentWord.hanzi, fontSize = 56.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(currentWord.pinyin, fontSize = 24.sp, color = Color.White.copy(0.8f))
                        Spacer(Modifier.height(20.dp))
                        IconButton(onClick = { speakChinese(tts, currentWord.hanzi) }) {
                            Icon(Icons.Default.VolumeUp, "Speak", tint = Color.White, modifier = Modifier.size(40.dp))
                        }
                    }
                } else {
                    /* BACK SIDE */
                    Column(
                        modifier = Modifier.graphicsLayer { rotationY = 180f },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentWord.urduScript,
                            fontFamily = urduFont,
                            fontSize = 38.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Text(currentWord.english, fontSize = 20.sp, color = Color.White.copy(0.8f))
                        Spacer(Modifier.height(20.dp))
                        IconButton(onClick = { speakUrdu(tts, currentWord.urduScript) }) {
                            Icon(Icons.Default.VolumeUp, "Speak", tint = Color.White, modifier = Modifier.size(40.dp))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationButton(
                enabled = currentIndex > 0,
                onClick = onPrev,
                icon = Icons.Default.ArrowBack,
                moduleColor = moduleColor
            )

            Text(
                text = "${currentIndex + 1} / ${vocabList.size}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = moduleColor
            )

            NavigationButton(
                enabled = currentIndex < vocabList.size - 1,
                onClick = onNext,
                icon = Icons.Default.ArrowForward,
                moduleColor = moduleColor
            )
        }
    }
}

@Composable
private fun NavigationButton(
    enabled: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    moduleColor: Color
) {
    ElevatedButton(
        onClick = onClick,
        enabled = enabled,
        shape = CircleShape,
        modifier = Modifier.size(64.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = moduleColor,
            contentColor = Color.White,
            disabledContainerColor = Color.LightGray.copy(alpha = 0.3f)
        )
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp))
    }
}

@Composable
private fun EmptyFlashcardMessage(padding: PaddingValues) {
    Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
        Text("No vocabulary available in this category.", textAlign = TextAlign.Center)
    }
}

private fun speakChinese(tts: TextToSpeech, text: String) {
    tts.language = Locale.CHINESE
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ZH")
}

private fun speakUrdu(tts: TextToSpeech, text: String) {
    tts.language = Locale("ur", "PK")
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "UR")
}