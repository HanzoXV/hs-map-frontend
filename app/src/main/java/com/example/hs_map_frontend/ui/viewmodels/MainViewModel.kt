package com.example.hs_map_frontend.ui.viewmodels

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hs_map_frontend.ui.screens.auth.AppLanguage

class MainViewModel : ViewModel () {
    var language by mutableStateOf(AppLanguage.URDU)
        private set

    // Public function to safely update the state
    fun setAppLanguage(newLanguage: AppLanguage) {
        language = newLanguage
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}