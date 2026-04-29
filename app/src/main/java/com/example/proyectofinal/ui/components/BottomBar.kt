package com.example.proyectofinal.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofinal.R
import com.example.proyectofinal.navigation.Routes

@Composable
fun BottomBar(navController: NavHostController, currentRoute: String?) {

    val itemColors = NavigationBarItemDefaults.colors(
        indicatorColor        = MaterialTheme.colorScheme.primaryContainer,
        selectedIconColor     = MaterialTheme.colorScheme.primary,
        selectedTextColor     = MaterialTheme.colorScheme.primary,
        unselectedIconColor   = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor   = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    Column {
        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
        ) {
            NavigationBarItem(
                selected = currentRoute == Routes.HOME,
                onClick  = {
                    if (currentRoute != Routes.HOME)
                        navController.navigate(Routes.HOME) { launchSingleTop = true }
                },
                icon   = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                label  = { Text("Inicio") },
                colors = itemColors
            )
            NavigationBarItem(
                selected = currentRoute == Routes.STATS,
                onClick  = {
                    if (currentRoute != Routes.STATS)
                        navController.navigate(Routes.STATS) { launchSingleTop = true }
                },
                icon = {
                    Icon(
                        painter            = painterResource(id = R.drawable.stats_icon),
                        contentDescription = "Estadísticas",
                        modifier           = Modifier.size(24.dp)
                    )
                },
                label  = { Text("Stats") },
                colors = itemColors
            )
            NavigationBarItem(
                selected = currentRoute == Routes.HISTORIAL,
                onClick  = {
                    if (currentRoute != Routes.HISTORIAL)
                        navController.navigate(Routes.HISTORIAL) { launchSingleTop = true }
                },
                icon   = { Icon(Icons.Default.List, contentDescription = "Historial") },
                label  = { Text("Historial") },
                colors = itemColors
            )
            NavigationBarItem(
                selected = currentRoute == Routes.CUENTA,
                onClick  = {
                    if (currentRoute != Routes.CUENTA)
                        navController.navigate(Routes.CUENTA) { launchSingleTop = true }
                },
                icon   = { Icon(Icons.Default.Person, contentDescription = "Mi cuenta") },
                label  = { Text("Cuenta") },
                colors = itemColors
            )
        }
    }
}
