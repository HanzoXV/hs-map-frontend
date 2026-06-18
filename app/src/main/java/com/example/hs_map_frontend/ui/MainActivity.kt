package com.example.hs_map_frontend.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hs_map_frontend.ui.navigation.Screen
import com.example.hs_map_frontend.ui.screens.auth.LoginScreen
import com.example.hs_map_frontend.ui.screens.auth.RegisterScreen
import com.example.hs_map_frontend.ui.screens.splash.SplashScreen
import com.example.hs_map_frontend.ui.theme.HSMapTheme
import com.example.hs_map_frontend.ui.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HSMapTheme(currentLanguage = mainViewModel.language) {
                AppNavigation(viewModel = mainViewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController,
                viewModel = viewModel)
        }

        composable(route = Screen.Dashboard.route) {
            // DashboardScreen can take the same viewModel parameter later if needed
        }
    }
}