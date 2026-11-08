package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Brand Colors: Pedro's Snack Emerald Green & Gold
val EmeraldDark = Color(0xFF082B21)
val EmeraldPrimary = Color(0xFF0A3C2F)
val EmeraldMedium = Color(0xFF115E48)
val EmeraldLight = Color(0xFF1E8266)
val EmeraldContainer = Color(0xFF0F493A)

val GoldPrimary = Color(0xFFD4AF37)
val GoldLight = Color(0xFFF3E5AB)
val GoldDark = Color(0xFFAA820A)
val GoldContainer = Color(0xFF382E0B)

val BrandRedBucket = Color(0xFFD32F2F)

// Luxury Dark Palette
val DarkBg = Color(0xFF0C120F)
val DarkSurface = Color(0xFF131B17)
val DarkSurfaceVariant = Color(0xFF1B2621)
val DarkCardBorder = Color(0x33D4AF37)
val DarkTextPrimary = Color(0xFFF4F6F5)
val DarkTextSecondary = Color(0xFFB0BCB6)

// Luxury Light Palette
val LightBg = Color(0xFFFBF9F6)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF2EFE9)
val LightCardBorder = Color(0x330A3C2F)
val LightTextPrimary = Color(0xFF0C1411)
val LightTextSecondary = Color(0xFF56635D)

// Accent Presets
enum class AccentPreset(val displayName: String, val color: Color) {
    GOLD("Imperial Gold", Color(0xFFD4AF37)),
    EMERALD("Emerald Glow", Color(0xFF10B981)),
    AMBER("Royal Amber", Color(0xFFF59E0B)),
    RUBY("Moroccan Ruby", Color(0xFFE11D48))
}
