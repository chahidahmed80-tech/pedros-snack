package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

class ThemeController(
    initialMode: ThemeMode = ThemeMode.DARK,
    initialAccent: AccentPreset = AccentPreset.GOLD
) {
    var mode by mutableStateOf(initialMode)
    var accent by mutableStateOf(initialAccent)
}

val LocalThemeController = compositionLocalOf { ThemeController() }

@Composable
fun PedrosSnackTheme(
    controller: ThemeController = LocalThemeController.current,
    content: @Composable () -> Unit
) {
    val isDark = when (controller.mode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val accentColor = controller.accent.color

    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = EmeraldMedium,
            onPrimary = Color.White,
            primaryContainer = EmeraldContainer,
            onPrimaryContainer = GoldLight,
            secondary = accentColor,
            onSecondary = Color.Black,
            secondaryContainer = GoldContainer,
            onSecondaryContainer = GoldLight,
            tertiary = BrandRedBucket,
            background = DarkBg,
            onBackground = DarkTextPrimary,
            surface = DarkSurface,
            onSurface = DarkTextPrimary,
            surfaceVariant = DarkSurfaceVariant,
            onSurfaceVariant = DarkTextSecondary
        )
    } else {
        lightColorScheme(
            primary = EmeraldPrimary,
            onPrimary = Color.White,
            primaryContainer = EmeraldMedium,
            onPrimaryContainer = Color.White,
            secondary = accentColor,
            onSecondary = Color.Black,
            secondaryContainer = GoldLight,
            onSecondaryContainer = GoldDark,
            tertiary = BrandRedBucket,
            background = LightBg,
            onBackground = LightTextPrimary,
            surface = LightSurface,
            onSurface = LightTextPrimary,
            surfaceVariant = LightSurfaceVariant,
            onSurfaceVariant = LightTextSecondary
        )
    }

    CompositionLocalProvider(LocalThemeController provides controller) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
