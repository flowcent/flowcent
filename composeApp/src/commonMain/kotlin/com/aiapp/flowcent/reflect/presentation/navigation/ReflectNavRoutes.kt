/*
 * Created by Saeedus Salehin on 15/5/25, 2:49 PM.
 */

package com.aiapp.flowcent.reflect.presentation.navigation

sealed class ReflectNavRoutes(val route: String) {
    data object ReflectHome : ReflectNavRoutes("reflect_home")
}