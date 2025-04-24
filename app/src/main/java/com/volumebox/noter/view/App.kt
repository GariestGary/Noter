package com.volumebox.noter.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("NavController not provided")
}

@Composable
fun App() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        AppNavigation(navController = navController)
    }
}