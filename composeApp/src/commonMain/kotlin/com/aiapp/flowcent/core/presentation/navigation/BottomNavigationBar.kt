/*
 * Created by Saeedus Salehin on 13/5/25, 2:41 PM.
 */

package com.aiapp.flowcent.core.presentation.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import com.aiapp.flowcent.core.domain.model.NavItem
import com.aiapp.flowcent.core.presentation.ui.theme.Colors

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
            val selected = index == selectedIndex

            // Animations
            val scale by animateFloatAsState(if (selected) 1.1f else 1f)
            val backgroundColor by animateColorAsState(
                if (selected) Colors.LightSurface.copy(alpha = 0.5f) else Color.Transparent
            )
            val borderColor = Colors.LightSurface

            val contentColor by animateColorAsState(
                if (selected) Colors.LightPrimary else Colors.LightSurface
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
                        color = backgroundColor,
                        shape = if (selected) RoundedCornerShape(24.dp) else CircleShape
                    )
                    // outline
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = if (selected) RoundedCornerShape(24.dp) else CircleShape
                    )
                    // padding: pill has horizontal padding, circle uses fixed size
                    .then(
                        if (selected) Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
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
                        tint = contentColor,
                        modifier = Modifier.size(24.dp)
                    )
                    if (selected) {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.titleMedium,
                            color = contentColor
                        )
                    }
                }
            }
        }
    }
}
