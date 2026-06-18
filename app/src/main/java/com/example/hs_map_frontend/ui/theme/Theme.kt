package com.example.hs_map_frontend.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

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
fun HSMapTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}