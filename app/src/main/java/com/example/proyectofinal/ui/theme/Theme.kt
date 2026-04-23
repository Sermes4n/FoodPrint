package com.example.proyectofinal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresApp = lightColorScheme(
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

    error          = Rojo,
    onError        = Color.White,
    errorContainer = RojoContainer,
    onErrorContainer = Color(0xFF7F1D1D),

    tertiary   = Verde2,
    onTertiary = Color.White,
)

@Composable
fun EcoScannerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColoresApp,
        typography  = AppTypography,
        content     = content
    )
}
