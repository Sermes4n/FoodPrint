package com.example.proyectofinal.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// Función para solicitar permisos GPS desde Compose
@Composable
fun rememberGpsPermissionLauncher(
    onLocationReceived: (Location) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineGranted || coarseGranted) {
            obtenerUbicacionActual(context, fusedLocationClient, onLocationReceived, onError)
        } else {
            onError("Es necesario el permiso de ubicación")
        }
    }

    fun requestLocation() {
        val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (hasFine || hasCoarse) {
            obtenerUbicacionActual(context, fusedLocationClient, onLocationReceived, onError)
        } else {
            launcher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    return requestLocation()
}

private fun obtenerUbicacionActual(
    context: Context,
    client: com.google.android.gms.location.FusedLocationProviderClient,
    onSuccess: (Location) -> Unit,
    onError: (String) -> Unit
) {
    try {
        client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    onSuccess(location)
                } else {
                    onError("No se pudo obtener ubicación. Activa el GPS.")
                }
            }
            .addOnFailureListener {
                onError("Error: ${it.message}")
            }
    } catch (e: SecurityException) {
        onError("Error de seguridad")
    }
}

// Función suspend para usar en ProductoScreen
suspend fun obtenerUbicacionSuspend(context: Context): Location? = suspendCoroutine { continuation ->
    val client = LocationServices.getFusedLocationProviderClient(context)
    val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    if (!hasFine && !hasCoarse) {
        continuation.resume(null)
        return@suspendCoroutine
    }

    client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        .addOnSuccessListener { location ->
            continuation.resume(location)
        }
        .addOnFailureListener {
            continuation.resume(null)
        }
}