package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
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
import com.example.proyectofinal.data.EscaneoConProducto
import com.example.proyectofinal.data.SupabaseRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private val filtros = listOf("Todos", "Esta semana", "Alto CO₂", "Bajo CO₂")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen() {

    var escaneos by remember { mutableStateOf<List<EscaneoConProducto>>(emptyList()) }
    var filtroSeleccionado by remember { mutableStateOf("Todos") }
    val scope = rememberCoroutineScope()

    fun cargar() {
        scope.launch {
            try { escaneos = SupabaseRepository.getEscaneos() } catch (_: Exception) { }
        }
    }

    LaunchedEffect(Unit) { cargar() }

    val hace7Dias = remember {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date(Date().time - 7 * 86400000L))
    }

    val escaneosFiltrados = remember(filtroSeleccionado, escaneos) {
        when (filtroSeleccionado) {
            "Esta semana" -> escaneos.filter { it.fecha >= hace7Dias }
            "Alto CO₂"   -> escaneos.filter { (it.co2Kg ?: it.producto.co2Kg) >= 20.0 }
            "Bajo CO₂"   -> escaneos.filter { (it.co2Kg ?: it.producto.co2Kg) < 10.0 }
            else          -> escaneos
        }
    }

    val totalCo2 = escaneosFiltrados.sumOf { it.co2Kg ?: it.producto.co2Kg }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = { Text("Historial", fontWeight = FontWeight.SemiBold) },
                actions = {
                    IconButton(onClick = { cargar() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Actualizar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        if (escaneosFiltrados.isEmpty() && filtroSeleccionado == "Todos") {
            EmptyHistorial(modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item {
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${escaneosFiltrados.size}",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        "Productos",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                VerticalDivider(
                                    modifier = Modifier
                                        .height(44.dp)
                                        .align(Alignment.CenterVertically),
                                    color = MaterialTheme.colorScheme.outline
                                )
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${"%.1f".format(totalCo2)} kg",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        "CO₂ total",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
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

                    if (escaneosFiltrados.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Sin resultados para este filtro",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        items(escaneosFiltrados) { escaneo ->
                            EscaneoCard(
                                escaneo = escaneo,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
        }
    }
}

@Composable
private fun EscaneoCard(escaneo: EscaneoConProducto, modifier: Modifier = Modifier) {
    val co2 = escaneo.co2Kg ?: escaneo.producto.co2Kg
    val (colorCo2, textoCo2) = when {
        co2 < 5  -> MaterialTheme.colorScheme.tertiary to "Bajo"
        co2 < 20 -> Color(0xFFF59E0B) to "Medio"
        else     -> MaterialTheme.colorScheme.error to "Alto"
    }

    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
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
                    text = escaneo.producto.nombre.firstOrNull()?.toString() ?: "?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    escaneo.producto.nombre,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Origen: ${escaneo.producto.origen}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    formatearFecha(escaneo.fecha),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${"%.1f".format(co2)} kg",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorCo2
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(colorCo2.copy(alpha = 0.1f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        textoCo2,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorCo2
                    )
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
            Text(
                "Sin productos escaneados",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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

private fun formatearFecha(isoFecha: String): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val fechaStr = isoFecha.substringBefore("+").trimEnd('Z').trim()
        val fecha = sdf.parse(fechaStr) ?: return isoFecha

        val ahora = Date()
        val diffMs = ahora.time - fecha.time
        val diffDias = (diffMs / 86400000L).toInt()

        val hora = SimpleDateFormat("HH:mm", Locale.getDefault()).format(fecha)

        when {
            diffDias == 0 -> "Hoy, $hora"
            diffDias == 1 -> "Ayer, $hora"
            diffDias < 30 -> "Hace $diffDias día${if (diffDias != 1) "s" else ""}"
            else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha)
        }
    } catch (e: Exception) {
        isoFecha
    }
}
