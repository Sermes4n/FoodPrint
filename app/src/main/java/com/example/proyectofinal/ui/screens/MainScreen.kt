package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.navigation.Routes
import com.example.proyectofinal.ui.components.BottomBar

@Composable
fun MainScreen() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN,
            modifier = Modifier.padding(padding)
        ) {

            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginClick = { navController.navigate(Routes.HOME) }
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    onEscanearClick = { navController.navigate(Routes.PRODUCTO) }
                )
            }

            composable(Routes.PRODUCTO) {
                ProductoScreen(
                    onVolverClick = { navController.popBackStack() }
                )
            }

            composable(Routes.HISTORIAL) {
                HistorialScreen()
            }

            composable(Routes.CUENTA) {
                CuentaScreen()
            }
        }
    }
}