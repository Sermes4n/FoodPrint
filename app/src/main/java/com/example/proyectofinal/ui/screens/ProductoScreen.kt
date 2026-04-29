package com.example.proyectofinal.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.proyectofinal.data.SupabaseRepository
import com.example.proyectofinal.ui.components.StatsViewModel
import com.example.proyectofinal.utils.BarcodeApiUtils
import com.example.proyectofinal.utils.LocationUtils
import com.example.proyectofinal.utils.obtenerUbicacionSuspend
import com.example.proyectofinal.utils.tienePermisoUbicacion
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoScreen(
    barcode: String,
    onVolverClick: () -> Unit,
    statsViewModel: StatsViewModel
) {
    val context = LocalContext.current

    var nombreProducto  by remember { mutableStateOf<String?>(null) }
    var paisOrigen      by remember { mutableStateOf<String?>(null) }
    var distanciaKm     by remember { mutableStateOf<Float?>(null) }
    var co2Kg           by remember { mutableStateOf<Double?>(null) }
    var cargando        by remember { mutableStateOf(true) }
    var errorMensaje    by remember { mutableStateOf<String?>(null) }
    var guardadoOk      by remember { mutableStateOf<Boolean?>(null) }
    var errorGuardado   by remember { mutableStateOf<String?>(null) }
    var paisesMap       by remember { mutableStateOf<Map<String, Pair<Double, Double>>>(emptyMap()) }
    var infoUbicacion   by remember { mutableStateOf<String?>(null) }

    // Tracks permission: starts true if already granted, updated by the launcher result
    var tienePermiso    by remember { mutableStateOf(tienePermisoUbicacion(context)) }
    // Becomes true once the permission dialog is dismissed (grant OR deny)
    var permisoResuelto by remember { mutableStateOf(tienePermisoUbicacion(context)) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        tienePermiso    = grants.values.any { it }
        permisoResuelto = true
    }

    LaunchedEffect(Unit) {
        if (barcode.isBlank()) {
            errorMensaje = "Código de barras inválido."
            cargando = false
            return@LaunchedEffect
        }

        // Load countries table and product in sequence
        paisesMap = SupabaseRepository.getPaises()

        val producto = BarcodeApiUtils.obtenerProductoDesdeAPI(barcode)
        if (producto == null) {
            errorMensaje = "Producto no encontrado en la base de datos (código: $barcode)."
            cargando = false
            return@LaunchedEffect
        }

        nombreProducto = producto.nombre
        val paisEfectivo = producto.pais ?: "España"
        paisOrigen       = paisEfectivo
        cargando         = false  // show product card immediately while we fetch location

        // --- Location + CO₂ calculation ---
        if (paisEfectivo == "España") {
            // KM 0: producto local, sin emisiones de transporte
            distanciaKm = 0f
            co2Kg       = 0.0
            statsViewModel.agregarProducto(0.0, 0f, esKm0 = true)
        } else {
            // Request location permission if not yet granted
            if (!tienePermiso) {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                // Suspend until the user responds (grant or deny)
                snapshotFlow { permisoResuelto }.first { it }
            }

            if (tienePermiso) {
                // 10 s timeout: avoids hanging on cold GPS or emulator
                val ubicacion = withTimeoutOrNull(10_000L) { obtenerUbicacionSuspend(context) }
                val coords    = LocationUtils.obtenerCoordenadasPais(paisEfectivo, paisesMap)

                when {
                    ubicacion == null ->
                        infoUbicacion = "No se pudo obtener el GPS. Activa la ubicación e inténtalo de nuevo."
                    coords == null ->
                        infoUbicacion = "El país \"$paisEfectivo\" no está en nuestra tabla de distancias."
                    else -> {
                        val (latP, lonP) = coords
                        val dist = LocationUtils.calcularDistancia(
                            ubicacion.latitude, ubicacion.longitude, latP, lonP
                        )
                        val co2 = LocationUtils.calcularCo2Kg(dist)
                        distanciaKm = dist
                        co2Kg       = co2
                        statsViewModel.agregarProducto(co2, dist, esKm0 = dist < 100f)
                    }
                }
            } else {
                infoUbicacion = "Permiso de ubicación denegado. No se puede calcular la huella de carbono."
            }
        }

        // Save to Supabase (always, even if location data is null)
        try {
            SupabaseRepository.guardarEscaneo(
                codigoBarras = barcode,
                nombre       = producto.nombre ?: "Desconocido",
                origen       = paisEfectivo,
                distanciaKm  = distanciaKm,
                co2Kg        = co2Kg
            )
            guardadoOk = true
        } catch (e: Exception) {
            guardadoOk = false
            errorGuardado = e.message
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultado del escaneo") },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
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

            // Loading spinner (while fetching product from API)
            if (cargando) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Buscando producto...",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                return@Column
            }

            // Error: product not found or blank barcode
            if (errorMensaje != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = errorMensaje!!,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 14.sp
                    )
                }
                return@Column
            }

            // Supabase save indicator
            if (guardadoOk == true) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "Escaneo guardado en tu historial.",
                        modifier = Modifier.padding(12.dp),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            } else if (guardadoOk == false) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Error al guardar: ${errorGuardado ?: "Comprueba tu conexión."}",
                        modifier = Modifier.padding(12.dp),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            // Product card
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = nombreProducto ?: "Producto desconocido",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Origen: ${paisOrigen ?: "Desconocido"}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Código: $barcode",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }

            // Distance and CO₂ metrics
            // Show spinner inside the cards while location is still being fetched
            val locationPending = !permisoResuelto || (tienePermiso && distanciaKm == null && infoUbicacion == null && guardadoOk == null)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (locationPending) {
                            CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                        } else {
                            Text(
                                text = if (distanciaKm != null) "%.0f km".format(distanciaKm) else "—",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            "Distancia",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Card(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (locationPending) {
                            CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                        } else {
                            Text(
                                text = if (co2Kg != null) "%.2f kg".format(co2Kg) else "—",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        Text(
                            "CO₂ emitido",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Location/permission warning
            if (infoUbicacion != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = infoUbicacion!!,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            // CO₂ equivalences
            if (co2Kg != null) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Esto equivale a...",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Build,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("%.1f km en coche".format(co2Kg!! / 0.21), fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "%.1f días de absorción de un árbol".format(co2Kg!! / (21.77 / 365.0)),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
