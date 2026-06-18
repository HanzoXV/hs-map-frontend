package com.example.hs_map_frontend.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.hs_map_frontend.ui.screens.auth.AppLanguage

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryTeal,
    background = MidnightBlack,
    surface = CardDark,
    onPrimary = MidnightBlack,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = TextError
)

@Composable
fun HSMapTheme(
    currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    content: @Composable () -> Unit
) {
    val layoutDirection = if (currentLanguage == AppLanguage.URDU) {
        LayoutDirection.Rtl
    } else {
        LayoutDirection.Ltr
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
            content()
        }
    }
}