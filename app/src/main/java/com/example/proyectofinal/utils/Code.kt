package com.example.proyectofinal.utils

import android.graphics.Bitmap
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume

data class ProductoAPI(
    val nombre: String?,
    val pais: String?
)

object BarcodeApiUtils {

    private val scanner = BarcodeScanning.getClient()

    suspend fun escanearCodigoBarras(bitmap: Bitmap): String? {
        val image = InputImage.fromBitmap(bitmap, 0)
        return suspendCancellableCoroutine { continuation ->
            val task = scanner.process(image)
            task.addOnSuccessListener { barcodes ->
                if (continuation.isActive) continuation.resume(barcodes.firstOrNull()?.rawValue)
            }
            task.addOnFailureListener {
                if (continuation.isActive) continuation.resume(null)
            }
        }
    }

    suspend fun obtenerProductoDesdeAPI(codigoBarras: String): ProductoAPI? {
        return withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            try {
                val url = URL(
                    "https://world.openfoodfacts.org/api/v2/product/$codigoBarras" +
                    "?fields=product_name,origins,origins_tags"
                )
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("User-Agent", "FoodPrint-Android/1.0 (atbermarket@gmail.com)")
                connection.connectTimeout = 8000
                connection.readTimeout = 8000

                when (connection.responseCode) {
                    200  -> { /* continue */ }
                    429  -> return@withContext null  // rate limited, retry later
                    else -> return@withContext null
                }

                val body = connection.inputStream.bufferedReader().readText()
                val json = JSONObject(body)

                if (json.optInt("status", 0) != 1) return@withContext null

                val product = json.getJSONObject("product")
                val nombre = product.optString("product_name").ifEmpty { null }
                val pais = extraerPais(product)

                ProductoAPI(nombre = nombre, pais = pais)

            } catch (e: Exception) {
                null
            } finally {
                connection?.disconnect()
            }
        }
    }

    private fun extraerPais(product: JSONObject): String? {
        // 1ª opción: origins_tags → campo normalizado, más fiable
        val tags = product.optJSONArray("origins_tags")
        if (tags != null) {
            for (i in 0 until tags.length()) {
                val pais = tagAPais(tags.getString(i))
                if (pais != null) return pais
            }
        }

        // 2ª opción: origins → texto libre ("France, Belgium" o "France > Normandy")
        val origins = product.optString("origins").ifEmpty { null }
        if (origins != null) {
            val paisTexto = origins
                .split(",", ">", "/", ";")
                .firstOrNull { it.isNotBlank() }
                ?.trim()
            if (!paisTexto.isNullOrBlank()) {
                return normalizarNombre(paisTexto)
            }
        }

        return null
    }

    // "en:united-kingdom" → "United Kingdom", filtra regiones genéricas
    private fun tagAPais(tag: String): String? {
        val sinPrefijo = if (tag.contains(":")) tag.substringAfter(":") else tag
        val nombre = normalizarNombre(sinPrefijo)
        val genericas = setOf(
            "European Union", "Eu", "World", "Unknown",
            "Not Specified", "Europe", "Asia", "Africa", "Americas"
        )
        return if (nombre in genericas) null else nombre
    }

    // "united-kingdom" → "United Kingdom", "FRANCE" → "France"
    private fun normalizarNombre(raw: String): String {
        return raw
            .replace("-", " ")
            .trim()
            .split(" ")
            .filter { it.isNotBlank() }
            .joinToString(" ") { word ->
                word.lowercase().replaceFirstChar { it.uppercaseChar() }
            }
    }
}
