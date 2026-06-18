package com.example.hs_map_frontend.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hs_map_frontend.ui.screens.auth.LoginScreen
import com.example.hs_map_frontend.ui.screens.auth.RegisterScreen
import com.example.hs_map_frontend.ui.screens.splash.SplashScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        /*composable(route = Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }*/
    }
}