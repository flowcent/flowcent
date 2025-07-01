/*
 * Created by Saeedus Salehin on 13/5/25, 2:41 PM.
 */

package com.aiapp.flowcent.core.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.domain.model.NavItem
import com.aiapp.flowcent.core.presentation.ui.theme.Colors
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
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Colors.White, shape = RoundedCornerShape(32.dp))
            .padding(vertical = 8.dp, horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            val selected = selectedIndex == index
            val bgColor by animateColorAsState(if (selected) Colors.LightPrimary else Colors.LightSurface)
            val scale by animateFloatAsState(if (selected) 1.1f else 1f)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .scale(scale)
                    .clickable { onItemSelected(index) }
            ) {
                Row(
                    modifier = Modifier
                        .size(48.dp)
                        .background(bgColor, shape = CircleShape),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = null,
                        tint = if (selected) Colors.White else Colors.LightPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (selected) Colors.LightPrimary else Colors.LightSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}