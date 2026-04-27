package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectofinal.ui.components.StatsViewModel

@Composable
fun StatsScreen(viewModel: StatsViewModel = viewModel()) {
    val porcentaje = viewModel.porcentajeKm0()
    val co2Ahorrado = viewModel.co2Ahorrado
    val kmReducidos = viewModel.kmReducidos
    val kmCocheEvitados = (co2Ahorrado / 0.12).toInt() // 0.12kg CO2 por km en coche

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "TU IMPACTO Y EVOLUCIÓN",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Gráfico circular
        Box(
            modifier = Modifier.size(180.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = porcentaje,
                modifier = Modifier.fillMaxSize(),
                strokeWidth = 16.dp,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "${(porcentaje * 100).toInt()}%",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "PRODUCTOS KM 0",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            "de tus productos son de proximidad",
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )

        // Card CO2
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("CO₂ ESTALVIAT FINS ARA", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text("${co2Ahorrado.toInt()} Kg", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                Text("Equival a ${kmCocheEvitados} km en cotxe evitats", fontSize = 13.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Card Kilómetros
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("KILÒMETRES DE TRANSPORT REDUÏTS", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text("${kmReducidos.toInt()} Km", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
                Text("Amb el teu ajut, el cercle creix", fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}