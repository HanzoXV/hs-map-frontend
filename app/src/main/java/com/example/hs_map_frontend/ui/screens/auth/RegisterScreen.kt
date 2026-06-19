package com.example.hs_map_frontend.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.hs_map_frontend.data.db_models.RegisterRequest
import com.example.hs_map_frontend.ui.components.AppTextField
import com.example.hs_map_frontend.ui.navigation.Screen
import com.example.hs_map_frontend.ui.viewmodels.AuthViewModel
import com.example.hs_map_frontend.ui.viewmodels.MainViewModel
import com.example.hs_map_frontend.ui.viewmodels.RegisterUiState

@Composable
fun RegisterScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    authViewModel: AuthViewModel
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Read the current network status emitted by our data pipeline
    val uiState by authViewModel.uiState

    // Handle single-fire navigation side-effects safely when registration succeeds
    LaunchedEffect(uiState) {
        if (uiState is RegisterUiState.Success) {
            // Clear backstack and drop user right into your Map Dashboard
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (mainViewModel.language == AppLanguage.URDU) "نیا اکاؤنٹ بنائیں" else "Create Account",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Display error text banners if validation or database conflicts occur
            if (uiState is RegisterUiState.Error) {
                Text(
                    text = (uiState as RegisterUiState.Error).error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontSize = 14.sp
                )
            }

            AppTextField(
                value = name,
                onValueChange = { name = it },
                placeholderText = if (mainViewModel.language == AppLanguage.URDU) "پورا نام" else "Full Name",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = email,
                onValueChange = { email = it },
                placeholderText = if (mainViewModel.language == AppLanguage.URDU) "ای میل ایڈریس" else "Email Address",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = password,
                onValueChange = { password = it },
                placeholderText = if (mainViewModel.language == AppLanguage.URDU) "پاس ورڈ" else "Password",
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        val request = RegisterRequest(
                            displayName = name,
                            email = email,
                            password = password,
                            preferredLanguage = mainViewModel.language.name
                        )
                        authViewModel.handleRegister(request)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = uiState !is RegisterUiState.Loading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (uiState is RegisterUiState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (mainViewModel.language == AppLanguage.URDU) "سائن اپ کریں" else "Sign Up",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (mainViewModel.language == AppLanguage.URDU) "کیا آپ کا پہلے سے اکاؤنٹ ہے؟ " else "Already have an account? ",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                Text(
                    text = if (mainViewModel.language == AppLanguage.URDU) "لاگ ان" else "Login",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}