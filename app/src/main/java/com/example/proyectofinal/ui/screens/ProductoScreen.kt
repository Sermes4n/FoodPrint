package com.example.proyectofinal.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.proyectofinal.ui.components.CameraViewModel
import com.example.proyectofinal.ui.components.StatsViewModel
import com.example.proyectofinal.utils.LocationUtils
import com.example.proyectofinal.utils.obtenerUbicacionSuspend


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen(onVolverClick: () -> Unit, viewModel: CameraViewModel, statsViewModel: StatsViewModel) {

    val image = viewModel.imageBitmap
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val bitmap = viewModel.imageBitmap

        if (bitmap != null) {
            // 1. Escanear código de barras
            val codigoBarras = BarcodeApiUtils.escanearCodigoBarras(bitmap)

            if (codigoBarras != null) {
                try {
                    // 2. LLAMADA HTTP A API (esto es lo que piden en la semana 3)
                    val productoInfo = MockApi.obtenerProductoMock(codigoBarras)

                    if (productoInfo != null) {
                        val pais = productoInfo.paisOrigen
                        val nombre = productoInfo.nombre

                        // 3. Obtener ubicación con GPS
                        val ubicacion = obtenerUbicacionSuspend(context)

                        if (ubicacion != null) {
                            val (latProd, lonProd) = LocationUtils.obtenerCoordenadasPais(pais)

                            val distancia = LocationUtils.calcularDistancia(
                                ubicacion.latitude, ubicacion.longitude,
                                latProd, lonProd
                            )

                            // 4. CÁLCULO DE CO2 (lógica interna)
                            val co2 = distancia * 0.0008
                            val esKm0 = distancia < 100

                            // 5. ACTUALIZAR UI con resultados
                            statsViewModel.agregarProducto(co2, distancia, esKm0)

                            // Mostrar nombre real del producto
                            // nombreProducto = nombre
                        }
                    }
                } catch (e: Exception) {
                    // Error de red o API
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultado") },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Yogur de Fresa", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Origen: Nueva Zelanda (Auckland)", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            if (image != null) {
                Image(
                    bitmap = image.asImageBitmap(),
                    contentDescription = "Foto producto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
            Text("Imagen: ${image != null}")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier.padding(14.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("32.000 km", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text("Distancia", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Card(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier.padding(14.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("75 kg CO₂", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                        Text("Emisiones", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Esto equivale a...", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Build, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("250 km en coche", fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Send, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("3 árboles para absorber este CO₂", fontSize = 14.sp)
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Tu ahorro acumulado", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("450 kg de CO₂", fontSize = 15.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                    Text("12.500 L de agua", fontSize = 15.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                }
            }

            Button(onClick = { }, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(8.dp)) {
                Text("Ver alternativa Km 0")
            }

            OutlinedButton(onClick = { }, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(8.dp)) {
                Text("Proponer mejora")
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
