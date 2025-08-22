package com.aiapp.flowcent.accounts.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import com.aiapp.flowcent.accounts.presentation.components.SelectableIcon


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