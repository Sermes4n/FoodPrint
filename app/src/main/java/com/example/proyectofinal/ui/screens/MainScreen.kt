package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.navigation.Routes
import com.example.proyectofinal.ui.components.BottomBar
import com.example.proyectofinal.ui.components.CameraViewModel
import com.example.proyectofinal.ui.components.StatsViewModel

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val cameraViewModel: CameraViewModel = viewModel()
    val statsViewModel: StatsViewModel = viewModel()

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
                    onLoginClick = { navController.navigate(Routes.HOME) },
                    onRegisterClick = {}
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    onEscanearClick = { navController.navigate(Routes.PRODUCTO) },
                )
            }

            composable(Routes.PRODUCTO) {
                ProductoScreen(
                    onVolverClick = { navController.popBackStack() },
                    viewModel = cameraViewModel,
                    viewModel = statsViewModel
                )
            }

            composable(Routes.HISTORIAL) {
                HistorialScreen()
            }

            composable(Routes.CUENTA) {
                CuentaScreen()
            }

            composable(Routes.STATS) {
                StatsScreen(statsViewModel)
            }
        }
    }
}

