package com.azeemi.chineaze.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.azeemi.chineaze.domain.model.CategoryData
import com.azeemi.chineaze.domain.model.ChineseCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCategoryClick: (Int) -> Unit
) {
    val categories = CategoryData.categories

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chineaze", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFDC143C),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(padding)
        ) {
            items(categories.size) { index ->
                CategoryCard(categories[index]) {
                    onCategoryClick(categories[index].id)
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: ChineseCategory,
    onClick: () -> Unit
) {
    // Improved Material 3 Tonal Palette
    val containerColor = when(category.id) {
        1 -> Color(0xFFFFDAD4) // Light Red/Coral
        2 -> Color(0xFFD1E8D1) // Soft Sage Green
        3 -> Color(0xFFD0E4FF) // Soft Sky Blue
        4 -> Color(0xFFF2D7FF) // Soft Lavender
        5 -> Color(0xFFFFDCC0) // Soft Peach/Brown
        6 -> Color(0xFF99F2EA) // Soft Cyan
        else -> Color(0xFFFFF1AC) // Soft Yellow
    }

    val contentColor = when(category.id) {
        1 -> Color(0xFF410002)
        2 -> Color(0xFF002105)
        else -> Color(0xFF1C1B1F)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp) // Adjusted height for better grid balance
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(28.dp), // Modern M3 rounding
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp, pressedElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.4f)
            ) {
                Image(
                    painter = painterResource(category.iconRes),
                    contentDescription = null,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

