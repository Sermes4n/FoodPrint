package com.example.proyectofinal.utils

object LocationUtils {

    fun calcularDistancia(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0] / 1000f
    }

    // Factor educativo: 0.0008 kg CO₂ por km transportado
    fun calcularCo2Kg(distanciaKm: Float): Double = distanciaKm * 0.0008

    fun obtenerCoordenadasPais(
        pais: String,
        paises: Map<String, Pair<Double, Double>>
    ): Pair<Double, Double>? {
        // Exact match first
        paises[pais]?.let { return it }
        // Case-insensitive fallback (handles "FRANCE", "france", "United kingdom", etc.)
        val paisLower = pais.lowercase()
        return paises.entries.firstOrNull { it.key.lowercase() == paisLower }?.value
    }
}
