package com.example.proyectofinal.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

fun tienePermisoUbicacion(context: Context): Boolean {
    val fine = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    val coarse = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    return fine || coarse
}

suspend fun obtenerUbicacionSuspend(context: Context): Location? {
    if (!tienePermisoUbicacion(context)) return null

    val client = LocationServices.getFusedLocationProviderClient(context)

    return try {
        suspendCancellableCoroutine { continuation ->
            try {
                val task = client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)

                task.addOnSuccessListener { location ->
                    if (!continuation.isActive) return@addOnSuccessListener
                    if (location != null) {
                        continuation.resume(location)
                    } else {
                        // GPS cold start or emulator: fall back to last known location
                        client.lastLocation
                            .addOnSuccessListener { last ->
                                if (continuation.isActive) continuation.resume(last)
                            }
                            .addOnFailureListener {
                                if (continuation.isActive) continuation.resume(null)
                            }
                    }
                }

                task.addOnFailureListener {
                    if (continuation.isActive) continuation.resume(null)
                }

            } catch (e: SecurityException) {
                if (continuation.isActive) continuation.resume(null)
            }
        }
    } catch (e: Exception) {
        null
    }
}
