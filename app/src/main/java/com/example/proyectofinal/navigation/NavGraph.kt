package com.example.proyectofinal.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.ui.components.CameraViewModel
import com.example.proyectofinal.ui.screens.CuentaScreen
import com.example.proyectofinal.ui.screens.HistorialScreen
import com.example.proyectofinal.ui.screens.HomeScreen
import com.example.proyectofinal.ui.screens.LoginScreen
import com.example.proyectofinal.ui.screens.ProductoScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val SCAN = "scan"
    const val PRODUCTO = "producto"
    const val HISTORIAL = "historial"
    const val CUENTA = "cuenta"

    const val STATS = "stats"

}
@Composable
fun NavGraph() {

    val navController = rememberNavController()
    val cameraViewModel: CameraViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = { navController.navigate(Routes.HOME) },
                onRegisterClick = { navController.navigate(Routes.REGISTER) }
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
                viewModel = cameraViewModel
            )
        }

        /*composable(Routes.HISTORIAL) {
            HistorialScreen(navController)
        }

        composable(Routes.CUENTA) {
            CuentaScreen(navController)
        }*/
    }
}
