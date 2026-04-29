package com.example.proyectofinal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

import com.example.proyectofinal.data.SupabaseRepository
import com.example.proyectofinal.ui.components.BottomBar
import com.example.proyectofinal.ui.components.StatsViewModel

import com.example.proyectofinal.ui.screens.*

object Routes {
    const val LOGIN     = "login"
    const val REGISTER  = "register"
    const val HOME      = "home"
    const val SCAN      = "scan"
    const val PRODUCTO  = "producto/{barcode}"
    const val HISTORIAL = "historial"
    const val CUENTA    = "cuenta"
    const val STATS     = "stats"
}

private val rutasSinBottomBar = setOf(Routes.LOGIN, Routes.REGISTER, Routes.SCAN)

@Composable
fun NavGraph() {

    val navController = rememberNavController()
    val statsViewModel: StatsViewModel = viewModel()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route
    val mostrarBottomBar = rutaActual !in rutasSinBottomBar

    val inicio = if (SupabaseRepository.estaLogueado()) Routes.HOME else Routes.LOGIN

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (mostrarBottomBar) BottomBar(navController, rutaActual)
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = inicio,
            modifier = Modifier.padding(padding)
        ) {

            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginClick = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onRegisterClick = { navController.navigate(Routes.REGISTER) }
                )
            }

            composable(Routes.REGISTER) {
                RegisterScreen(
                    onRegisterClick = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
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
                    onBarcodeDetected = { barcode ->
                        navController.navigate("producto/$barcode") {
                            popUpTo(Routes.SCAN) { inclusive = true }
                        }
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.PRODUCTO,
                arguments = listOf(navArgument("barcode") { type = NavType.StringType })
            ) { backStackEntry ->
                val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
                ProductoScreen(
                    barcode = barcode,
                    onVolverClick = { navController.popBackStack() },
                    statsViewModel = statsViewModel
                )
            }

            composable(Routes.STATS) {
                StatsScreen()
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
