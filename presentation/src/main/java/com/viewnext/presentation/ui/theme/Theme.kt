package com.viewnext.presentation.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = HoloGreenLight,
    onPrimary = White,
    primaryContainer = HoloGreenDark,

    secondary = GreenSecondary,
    onSecondary = Black,
    secondaryContainer = GreenSecondaryDark,

    background = White,
    surface = White,
    onBackground = Black,
    onSurface = Black
)

@Composable
fun EnergyAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}