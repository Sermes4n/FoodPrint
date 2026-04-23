package com.example.proyectofinal.utils

object BarcodeUtils {

    fun obtenerPaisDesdeEAN(ean: String): String {
        val prefijo = ean.take(2).toIntOrNull() ?: return "Desconocido"

        return when (prefijo) {
            in 84..84 -> "España"
            in 30..37 -> "Francia"
            in 40..44 -> "Alemania"
            in 50..50 -> "Reino Unido"
            in 80..83 -> "Italia"
            in 94..94 -> "Nueva Zelanda"
            else -> "Desconocido"
        }
    }
}