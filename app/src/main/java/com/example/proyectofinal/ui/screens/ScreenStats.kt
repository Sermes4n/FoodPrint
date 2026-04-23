package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinal.ui.components.StatsViewModel

@Composable
fun StatsScreen(viewModel: StatsViewModel = viewModel()) {

    val porcentaje = viewModel.porcentajeKm0()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Tu impacto y evolución",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(contentAlignment = Alignment.Center) {

            CircularProgressIndicator(
                progress = porcentaje,
                strokeWidth = 12.dp,
                modifier = Modifier.size(150.dp)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${(porcentaje * 100).toInt()}%")
                Text("Productos Km 0")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("CO2 estalviat fins ara")
                Text("${viewModel.co2Ahorrado} Kg", fontSize = 22.sp)
                Text("Equivale a X km en coche evitados")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Kilómetros de transporte reducidos")
                Text("${viewModel.kmReducidos} Km", fontSize = 22.sp)
                Text("Con tu ayuda, el círculo crece")
            }
        }
    }
}