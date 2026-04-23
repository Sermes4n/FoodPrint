package com.example.proyectofinal.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class StatsViewModel : ViewModel() {

    var co2Ahorrado by mutableStateOf(0.0)
    var kmReducidos by mutableStateOf(0.0)
    var productosKm0 by mutableStateOf(0)
    var totalProductos by mutableStateOf(0)

    fun agregarProducto(co2: Double, km: Double, esKm0: Boolean) {
        co2Ahorrado += co2
        kmReducidos += km
        totalProductos++

        if (esKm0) productosKm0++
    }

    fun porcentajeKm0(): Float {
        return if (totalProductos == 0) 0f
        else productosKm0.toFloat() / totalProductos
    }
}