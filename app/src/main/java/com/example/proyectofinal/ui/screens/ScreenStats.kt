package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.data.EscaneoConProducto
import com.example.proyectofinal.data.SupabaseRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen() {

    var escaneos by remember { mutableStateOf<List<EscaneoConProducto>>(emptyList()) }
    val scope = rememberCoroutineScope()

    fun cargar() {
        scope.launch {
            try { escaneos = SupabaseRepository.getEscaneos() } catch (_: Exception) { }
        }
    }

    LaunchedEffect(Unit) { cargar() }

    val totalProductos     = escaneos.size
    val productosKm0       = escaneos.count { (it.distanciaKm ?: Float.MAX_VALUE) < 100f }
    val porcentaje         = if (totalProductos == 0) 0f else productosKm0.toFloat() / totalProductos
    val co2Total           = escaneos.sumOf { it.co2Kg ?: it.producto.co2Kg }
    val kmTotales          = escaneos.sumOf { (it.distanciaKm ?: 0f).toDouble() }
    val kmCocheEquivalentes = (co2Total / 0.21).toInt()
    val co2Medio           = if (totalProductos == 0) 0.0 else co2Total / totalProductos

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text       = "Tu Impacto",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor     = MaterialTheme.colorScheme.primary,
                    titleContentColor  = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            // ── KM 0 ring ────────────────────────────────────────────────────
            Box(
                modifier        = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress    = { porcentaje },
                    modifier    = Modifier.fillMaxSize(),
                    strokeWidth = 18.dp,
                    color       = MaterialTheme.colorScheme.primary,
                    trackColor  = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text       = "${(porcentaje * 100).toInt()}%",
                        fontSize   = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text  = "KM 0",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text  = "$productosKm0 de $totalProductos productos son de proximidad",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ── CO₂ + KM cards ──────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // CO₂ total
                Card(
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(16.dp),
                    colors   = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text  = "CO₂ TOTAL",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.65f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text       = "${"%.1f".format(co2Total)}",
                            fontSize   = 34.sp,
                            fontWeight = FontWeight.Bold,
                            color      = MaterialTheme.colorScheme.primary,
                            lineHeight = 34.sp
                        )
                        Text(
                            text  = "kg registrados",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.60f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text  = "≈ $kmCocheEquivalentes km en coche",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
                        )
                    }
                }

                // KM total
                Card(
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text  = "KM TOTALES",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text       = "${kmTotales.toInt()}",
                            fontSize   = 34.sp,
                            fontWeight = FontWeight.Bold,
                            color      = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 34.sp
                        )
                        Text(
                            text  = "km de transporte",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text  = "$totalProductos productos totales",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Bottom stat cards ────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Products scanned
                Card(
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier            = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text       = "$totalProductos",
                            fontSize   = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color      = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text  = "Escaneados",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Average CO₂
                Card(
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier            = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text       = "${"%.2f".format(co2Medio)}",
                            fontSize   = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color      = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text  = "kg CO₂ medio",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
