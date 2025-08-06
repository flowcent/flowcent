package com.aiapp.flowcent.core.domain.model

import org.jetbrains.compose.resources.DrawableResource

data class NavItem(
    val label: String,
    val icon: DrawableResource,
    val selectedIcon: DrawableResource,
    val route: String
)