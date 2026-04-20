package com.example.proyectofinal
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.proyectofinal.navigation.NavGraph
import com.example.proyectofinal.ui.screens.MainScreen
import com.example.proyectofinal.ui.theme.EcoScannerTheme
import androidx.core.view.WindowCompat


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            EcoScannerTheme {
                MainScreen()
            }
        }
    }
}
