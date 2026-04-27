import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import android.graphics.Bitmap

object BarcodeApiUtils {

    private val scanner: BarcodeScanner = BarcodeScanning.getClient()

    suspend fun escanearCodigoBarras(bitmap: Bitmap): String? {
        val image = InputImage.fromBitmap(bitmap, 0)

        return suspendCoroutine { continuation ->
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    val codigo = barcodes.firstOrNull()?.rawValue
                    continuation.resume(codigo)
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
    }

    // Aquí llamarías a tu API real con el código de barras
    suspend fun obtenerPaisDesdeAPI(codigoBarras: String): String? {
        // TODO: Llamada a tu API
        // val response = apiService.getProductInfo(codigoBarras)
        // return response.paisOrigen

        // Por ahora, simulación mejorada
        return when {
            codigoBarras.startsWith("84") -> "España"
            codigoBarras.startsWith("30") -> "Francia"
            codigoBarras.startsWith("40") -> "Alemania"
            codigoBarras.startsWith("50") -> "Reino Unido"
            codigoBarras.startsWith("80") -> "Italia"
            codigoBarras.startsWith("94") -> "Nueva Zelanda"
            else -> null
        }
    }
}