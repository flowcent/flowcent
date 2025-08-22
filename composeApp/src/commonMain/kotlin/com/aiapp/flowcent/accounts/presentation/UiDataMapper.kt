package com.aiapp.flowcent.accounts.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.aiapp.flowcent.accounts.presentation.components.SelectableIcon
import kotlin.random.Random


fun getAccountIcons(): List<SelectableIcon> {
    return listOf(
        SelectableIcon(id = 1111, icon = Icons.Default.Home),
        SelectableIcon(id = 1112, icon = Icons.Default.Groups),
        SelectableIcon(id = 1113, icon = Icons.Default.Favorite),
        SelectableIcon(id = 1114, icon = Icons.Default.Work),
        SelectableIcon(id = 1115, icon = Icons.Default.Flight),
        SelectableIcon(id = 1116, icon = Icons.Default.ShoppingCart),
    )
}

fun getAccountIcon(id: Int): SelectableIcon {
    return getAccountIcons().find { it.id == id } ?: getAccountIcons().first()
}


val gradientPalette = listOf(
    listOf(Color(0xFF2563EB), Color(0xFF60A5FA)), // Blue
    listOf(Color(0xFF16A34A), Color(0xFF4ADE80)), // Green
    listOf(Color(0xFFFF6B00), Color(0xFFFFA94D)), // Orange
    listOf(Color(0xFF9333EA), Color(0xFFD8B4FE)), // Purple
    listOf(Color(0xFFEF4444), Color(0xFFFCA5A5)), // Red
    listOf(Color(0xFF0EA5E9), Color(0xFF7DD3FC)), // Sky Blue
    listOf(Color(0xFFFACC15), Color(0xFFFFF382)), // Yellow
    listOf(Color(0xFFDB2777), Color(0xFFF9A8D4)), // Pink
    listOf(Color(0xFF10B981), Color(0xFF6EE7B7)), // Teal Green
    listOf(Color(0xFFF97316), Color(0xFFFFC58B)), // Orange Light
    listOf(Color(0xFF8B5CF6), Color(0xFFD8B4FE)), // Violet
    listOf(Color(0xFF4B5563), Color(0xFF9CA3AF)), // Gray
    listOf(Color(0xFF3B82F6), Color(0xFF93C5FD)), // Blue Light
    listOf(Color(0xFF14B8A6), Color(0xFF81E6D9)), // Turquoise
    listOf(Color(0xFFF43F5E), Color(0xFFFCA5A5)), // Coral
    listOf(Color(0xFFFBBF24), Color(0xFFFFF9C3)), // Golden Yellow
    listOf(Color(0xFFEC4899), Color(0xFFF9A8D4)), // Magenta
    listOf(Color(0xFF6366F1), Color(0xFFA5B4FC)), // Indigo
    listOf(Color(0xFF22D3EE), Color(0xFF67E8F9)), // Cyan
    listOf(Color(0xFFFB7185), Color(0xFFFECACA))  // Rose
)


fun randomGradient(): Brush {
    val colors = gradientPalette.random()
    return Brush.linearGradient(colors)
}

@Composable
fun randomColor(): Color {
    val colors = listOf(
        Color(0xFFE57373),
        Color(0xFF64B5F6),
        Color(0xFF81C784),
        Color(0xFFFFB74D),
        Color(0xFFBA68C8)
    )
    return colors[Random.nextInt(colors.size)]
}