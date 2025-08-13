/*
 * Created by Saeedus Salehin on 13/8/25, 12:57 PM.
 */

package com.aiapp.flowcent.core.presentation.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalNavController = compositionLocalOf<NavHostController> {
    error("No NavController provided")
}
