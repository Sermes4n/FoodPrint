package com.example.proyectofinal.utils

import android.annotation.SuppressLint
import android.content.Context
import android.health.connect.datatypes.ExerciseRoute
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
fun obtenerUbicacion(
    context: Context,
    onLocationReceived: (ExerciseRoute.Location) -> Unit
) {
    val client = LocationServices.getFusedLocationProviderClient(context)

    client.lastLocation.addOnSuccessListener { location ->
        location?.let { onLocationReceived(it) }
    }
}