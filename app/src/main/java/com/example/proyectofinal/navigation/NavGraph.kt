package com.example.proyectofinal.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*

import com.example.proyectofinal.ui.components.BottomBar
import com.example.proyectofinal.ui.components.CameraViewModel
import com.example.proyectofinal.ui.components.StatsViewModel

import com.example.proyectofinal.ui.screens.*

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val PRODUCTO = "producto"
    const val HISTORIAL = "historial"
    const val CUENTA = "cuenta"
    const val STATS = "stats"
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun NavGraph() {

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
                    onEscanearClick = {
                        navController.navigate(Routes.PRODUCTO)
                    }
                )
            }

            composable(Routes.PRODUCTO) {
                ProductoScreen(
                    onVolverClick = { navController.popBackStack() },
                    cameraViewModel = cameraViewModel,
                    statsViewModel = statsViewModel
                )
            }

            composable(Routes.STATS) {
                StatsScreen(statsViewModel)
            }

            composable(Routes.HISTORIAL) {
                HistorialScreen()
            }

            composable(Routes.CUENTA) {
                CuentaScreen(
                    onLogoutClick = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0)
                        }
                    }
                )
            }
        }
    }
}

