package com.example.proyectofinal.utils

object LocationUtils {

    fun calcularDistancia(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Float {

        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            lat1, lon1,
            lat2, lon2,
            results
        )
        return results[0] / 1000
    }

    fun obtenerCoordenadasPais(pais: String): Pair<Double, Double> {
        return when (pais) {
            "España" -> Pair(40.4168, -3.7038)
            "Francia" -> Pair(48.8566, 2.3522)
            "Alemania" -> Pair(52.5200, 13.4050)
            "Italia" -> Pair(41.9028, 12.4964)
            "Nueva Zelanda" -> Pair(-36.8485, 174.7633)
            else -> Pair(0.0, 0.0)
        }
    }
}