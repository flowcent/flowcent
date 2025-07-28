/*
 * Created by Saeedus Salehin on 13/5/25, 2:41 PM.
 */

package com.aiapp.flowcent.core.presentation.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.domain.model.NavItem
import org.jetbrains.compose.resources.painterResource


@Composable
fun BottomNavigationBar(
    items: List<NavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = index == selectedIndex

            // Animations
            val scale by animateFloatAsState(if (isSelected) 1.1f else 1f, label = "scale")

            val isDarkTheme = isSystemInDarkTheme()


            val gradientBrush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF00C853), // Vivid green (mid-saturation, not too neon)
                    Color(0xFF2196F3)  // Bright, clean blue (material blue 500)
                )
            )

            Box(
                modifier = Modifier
                    .scale(scale)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) { onItemSelected(index) }
                    // shape + background
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = if (isSelected) RoundedCornerShape(24.dp) else CircleShape
                    )
                    // outline
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.05f),
                        shape = if (isSelected) RoundedCornerShape(24.dp) else CircleShape
                    )
                    // padding: pill has horizontal padding, circle uses fixed size
                    .then(
                        if (isSelected) Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        else Modifier.size(48.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                            .graphicsLayer(alpha = 0.99f) // force layer rendering
                            .drawWithCache {
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(
                                        gradientBrush,
                                        blendMode = BlendMode.SrcAtop
                                    )
                                }
                            },
                    )
                    if (isSelected) {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.titleMedium.copy(
                                brush = gradientBrush,
                                fontWeight = FontWeight.Medium
                            ),
                        )
                    }
                }
            }
        }
    }
}