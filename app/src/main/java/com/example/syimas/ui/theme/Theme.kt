package com.example.syimas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// We only build a light theme because your Figma design is light.
private val LightColors = lightColorScheme(
    primary        = Green,
    onPrimary      = White,
    background     = White,
    onBackground   = TextBlack,
    surface        = White,
    onSurface      = TextBlack,
    surfaceVariant = LightGray
)

@Composable
fun SYIMaSTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography  = AppTypography,
        content     = content
    )
}