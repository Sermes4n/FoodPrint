package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.navigation.Routes
import com.example.proyectofinal.ui.components.BottomBar

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute != Routes.LOGIN && currentRoute != Routes.REGISTER && currentRoute != Routes.SCAN

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController)
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN,
            modifier = Modifier.padding(padding)
        ) {

            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginClick = { navController.navigate(Routes.HOME) },
                    onRegisterClick = { navController.navigate(Routes.REGISTER) }
                )
            }

            composable(Routes.REGISTER) {
                RegisterScreen(
                    onRegisterClick = { navController.navigate(Routes.HOME) },
                    onBackToLoginClick = { navController.popBackStack() }
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    onEscanearClick = { navController.navigate(Routes.SCAN) }
                )
            }

            composable(Routes.SCAN) {
                BarcodeScanScreen(
                    onBarcodeDetected = { navController.navigate(Routes.PRODUCTO) },
                    onBackClick = { navController.popBackStack() }
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
                CuentaScreen(
                    onLogoutClick = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
