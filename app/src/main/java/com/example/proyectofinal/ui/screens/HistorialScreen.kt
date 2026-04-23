package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.ui.components.AppCard

private data class ProductoHistorial(
    val nombre: String,
    val origen: String,
    val fecha: String,
    val co2Kg: Double
)

private val historialEjemplo = listOf(
    ProductoHistorial("Yogur de Fresa",  "Nueva Zelanda", "Hoy, 14:32",     75.0),
    ProductoHistorial("Leche Entera",    "Países Bajos",  "Hoy, 11:05",      4.2),
    ProductoHistorial("Zumo de Naranja", "Brasil",        "Ayer, 19:48",    38.5),
    ProductoHistorial("Manzanas",        "Chile",         "Ayer, 10:20",    12.1),
    ProductoHistorial("Pan de Molde",    "España",        "Hace 2 días",     1.8),
    ProductoHistorial("Salmón Ahumado",  "Noruega",       "Hace 2 días",     6.4),
    ProductoHistorial("Café Molido",     "Colombia",      "Hace 3 días",     9.7),
    ProductoHistorial("Queso Gouda",     "Países Bajos",  "Hace 4 días",    11.2),
    ProductoHistorial("Uvas",            "Sudáfrica",     "Hace 5 días",    28.3),
    ProductoHistorial("Arroz Largo",     "Tailandia",     "Hace 6 días",     3.1),
)

private val filtros = listOf("Todos", "Esta semana", "Alto CO₂", "Bajo CO₂")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen() {

    var filtroSeleccionado by remember { mutableStateOf("Todos") }

    val productosFiltrados = remember(filtroSeleccionado) {
        when (filtroSeleccionado) {
            "Alto CO₂"    -> historialEjemplo.filter { it.co2Kg >= 20 }
            "Bajo CO₂"    -> historialEjemplo.filter { it.co2Kg < 10 }
            "Esta semana" -> historialEjemplo.take(6)
            else          -> historialEjemplo
        }
    }

    val totalCo2 = productosFiltrados.sumOf { it.co2Kg }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        if (productosFiltrados.isEmpty()) {
            EmptyHistorial(modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {

                item {
                    AppCard(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${productosFiltrados.size}",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text("Productos", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            VerticalDivider(
                                modifier = Modifier.height(44.dp).align(Alignment.CenterVertically),
                                color = MaterialTheme.colorScheme.outline
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${"%.1f".format(totalCo2)} kg",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text("CO₂ total", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }

                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        items(filtros) { filtro ->
                            FilterChip(
                                selected = filtroSeleccionado == filtro,
                                onClick = { filtroSeleccionado = filtro },
                                label = { Text(filtro, fontSize = 13.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }
                }

                items(productosFiltrados) { producto ->
                    ProductoCard(
                        producto = producto,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductoCard(producto: ProductoHistorial, modifier: Modifier = Modifier) {
    val (colorCo2, textoCo2) = when {
        producto.co2Kg < 5  -> MaterialTheme.colorScheme.tertiary to "Bajo"
        producto.co2Kg < 20 -> Color(0xFFF59E0B) to "Medio"
        else                -> MaterialTheme.colorScheme.error to "Alto"
    }

    AppCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = producto.nombre.first().toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("Origen: ${producto.origen}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(producto.fecha, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text("${"%.1f".format(producto.co2Kg)} kg", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colorCo2)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(colorCo2.copy(alpha = 0.1f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(textoCo2, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = colorCo2)
                }
            }
        }
    }
}

@Composable
private fun EmptyHistorial(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Sin productos escaneados", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Escanea tu primer producto para\nver su huella de carbono aquí",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                lineHeight = 18.sp
            )
        }
    }
}
