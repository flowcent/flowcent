/*
 * Created by Saeedus Salehin on 13/5/25, 2:41 PM.
 */

package com.aiapp.flowcent.core.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    NavigationBar(
        containerColor = Colors.White
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(item.icon), contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = { Text(item.label) },
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}