package com.example.proyectofinal.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ColoresApp = lightColorScheme(
    primary = Verde,
    secondary = VerdeClarito
)

@Composable
fun EcoScannerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColoresApp,
        content = content
    )
}
