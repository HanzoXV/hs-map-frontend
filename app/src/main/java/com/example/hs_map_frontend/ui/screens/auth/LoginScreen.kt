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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.hs_map_frontend.data.db_models.LoginRequest
import com.example.hs_map_frontend.ui.components.AppTextField
import com.example.hs_map_frontend.ui.navigation.Screen
import com.example.hs_map_frontend.ui.viewmodels.*

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    authViewModel: AuthViewModel,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf("") }

    val loginState by authViewModel.loginState

    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginUiState.Success -> {
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
                authViewModel.resetLoginState()
            }
            is LoginUiState.Error -> {
                localError = state.error
                authViewModel.resetLoginState()
            }
            else -> Unit
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
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { viewModel.setAppLanguage(AppLanguage.URDU) }
                    ) {
                        RadioButton(
                            selected = (viewModel.language == AppLanguage.URDU),
                            onClick = { viewModel.setAppLanguage(AppLanguage.URDU) },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                        )
                        Text(
                            text = "اردو",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp,
                            fontWeight = if (viewModel.language == AppLanguage.URDU) FontWeight.Bold else FontWeight.Normal
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { viewModel.setAppLanguage(AppLanguage.ENGLISH) }
                    ) {
                        RadioButton(
                            selected = (viewModel.language == AppLanguage.ENGLISH),
                            onClick = { viewModel.setAppLanguage(AppLanguage.ENGLISH) },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                        )
                        Text(
                            text = "English",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp,
                            fontWeight = if (viewModel.language == AppLanguage.ENGLISH) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Text(
                text = if (viewModel.language == AppLanguage.URDU) "خوش آمدید" else "Welcome Back",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (viewModel.language == AppLanguage.URDU) "ڈیش بورڈ تک رسائی کے لیے سائن ان کریں" else "Sign in to access your dashboard",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            AppTextField(
                value = email,
                onValueChange = { email = it },
                placeholderText = if (viewModel.language == AppLanguage.URDU) "ای میل ایڈریس" else "Email Address",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = password,
                onValueChange = { password = it },
                placeholderText = if (viewModel.language == AppLanguage.URDU) "پاس ورڈ" else "Password",
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        localError = if (viewModel.language == AppLanguage.URDU)
                            "تمام فیلڈز کو بھرنا ضروری ہے"
                        else
                            "All fields must be filled."
                    } else {
                        localError = ""
                        authViewModel.handleLogin(LoginRequest(email, password))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = loginState !is LoginUiState.Loading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (loginState is LoginUiState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (viewModel.language == AppLanguage.URDU) "لاگ ان کریں" else "Login",
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
                    text = if (viewModel.language == AppLanguage.URDU) "کیا آپ کا اکاؤنٹ نہیں ہے؟ " else "Don't have an account? ",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                Text(
                    text = if (viewModel.language == AppLanguage.URDU) "رجسٹر کریں" else "Register",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (localError.isNotEmpty()) {
                Text(
                    text = localError,
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

enum class AppLanguage {
    URDU, ENGLISH
}