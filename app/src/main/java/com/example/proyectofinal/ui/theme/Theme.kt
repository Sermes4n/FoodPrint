package com.example.proyectofinal.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small      = RoundedCornerShape(10.dp),
    medium     = RoundedCornerShape(12.dp),
    large      = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(20.dp)
)

private val LightColors = lightColorScheme(
    primary              = Verde,
    onPrimary            = Color.White,
    primaryContainer     = PrimaryContainer,
    onPrimaryContainer   = Verde1,

    secondary            = VerdeClarito,
    onSecondary          = Color.White,
    secondaryContainer   = PrimaryContainer,
    onSecondaryContainer = Verde1,

    background           = Background,
    onBackground         = Foreground,

    surface                  = CardBackground,
    onSurface                = Foreground,
    surfaceVariant           = MutedBackground,
    onSurfaceVariant         = MutedForeground,
    surfaceContainerLowest   = CardBackground,
    surfaceContainerLow      = CardBackground,
    surfaceContainer         = CardBackground,
    surfaceContainerHigh     = MutedBackground,
    surfaceContainerHighest  = Border,

    outline        = Border,
    outlineVariant = MutedBackground,

    error            = Rojo,
    onError          = Color.White,
    errorContainer   = RojoContainer,
    onErrorContainer = Color(0xFF7F1D1D),

    tertiary   = Verde2,
    onTertiary = Color.White,
)

private val DarkColors = darkColorScheme(
    primary              = Verde2,
    onPrimary            = Color.White,
    primaryContainer     = DarkPrimaryContainer,
    onPrimaryContainer   = DarkVerde1,

    secondary            = VerdeClarito,
    onSecondary          = Color.White,
    secondaryContainer   = DarkPrimaryContainer,
    onSecondaryContainer = DarkVerde1,

    background           = DarkBackground,
    onBackground         = DarkForeground,

    surface                  = DarkCardBackground,
    onSurface                = DarkForeground,
    surfaceVariant           = DarkMutedBackground,
    onSurfaceVariant         = DarkMutedForeground,
    surfaceContainerLowest   = DarkCardBackground,
    surfaceContainerLow      = DarkCardBackground,
    surfaceContainer         = DarkCardBackground,
    surfaceContainerHigh     = DarkMutedBackground,
    surfaceContainerHighest  = DarkBorder,

    outline        = DarkBorder,
    outlineVariant = DarkMutedBackground,

    error            = DarkRojo,
    onError          = Color.White,
    errorContainer   = DarkRojoContainer,
    onErrorContainer = Color(0xFFFEE2E2),

    tertiary   = Verde3,
    onTertiary = Color.White,
)

@Composable
fun EcoScannerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography  = AppTypography,
        shapes      = AppShapes,
        content     = content
    )
}
