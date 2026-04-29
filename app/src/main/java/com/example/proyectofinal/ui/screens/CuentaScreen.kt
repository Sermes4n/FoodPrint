package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.data.EscaneoConProducto
import com.example.proyectofinal.data.Profile
import com.example.proyectofinal.data.SupabaseRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private const val POLITICA_PRIVACIDAD = """Política de Privacidad

Última actualización: abril 2026

FoodPrint recoge únicamente los datos necesarios para ofrecerte el servicio: correo electrónico, nombre de usuario y el historial de productos escaneados junto con su huella de CO₂.

Tus datos se almacenan de forma segura en Supabase y nunca se venden ni comparten con terceros con fines comerciales. Solo accedemos a tu ubicación en el momento del escaneo para calcular la distancia al origen del producto; dicha ubicación no se almacena de forma permanente.

Puedes solicitar la eliminación de tu cuenta y todos tus datos enviando un correo a atbermarket@gmail.com. Nos comprometemos a gestionar tu solicitud en un plazo máximo de 30 días."""

private const val TERMINOS_CONDICIONES = """Términos y Condiciones

Última actualización: abril 2026

Al usar FoodPrint aceptas las siguientes condiciones:

1. Uso permitido. FoodPrint es una aplicación educativa y de concienciación medioambiental. Queda prohibido su uso con fines comerciales o fraudulentos.

2. Exactitud de los datos. Los valores de CO₂ son estimaciones basadas en datos públicos y pueden no reflejar con exactitud la huella real de cada producto.

3. Responsabilidad. FoodPrint se ofrece "tal cual" y no garantiza la disponibilidad continua del servicio. No nos hacemos responsables de decisiones de compra tomadas en base a la información mostrada.

4. Modificaciones. Podemos actualizar estos términos en cualquier momento. Te notificaremos los cambios relevantes en la próxima apertura de la app.

5. Contacto. Para cualquier consulta escríbenos a atbermarket@gmail.com."""

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuentaScreen(onLogoutClick: () -> Unit) {

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showPrivacidadDialog by remember { mutableStateOf(false) }
    var showTerminosDialog by remember { mutableStateOf(false) }
    var perfil by remember { mutableStateOf<Profile?>(null) }
    var escaneos by remember { mutableStateOf<List<EscaneoConProducto>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try { perfil  = SupabaseRepository.getProfile()  } catch (_: Exception) { }
        try { escaneos = SupabaseRepository.getEscaneos() } catch (_: Exception) { }
    }

    val nombre = perfil?.nombre
        ?: SupabaseRepository.getUsuarioEmail()?.substringBefore("@")
        ?: "Usuario"
    val email = perfil?.email
        ?: SupabaseRepository.getUsuarioEmail()
        ?: ""
    val iniciales = nombre.split(" ")
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifBlank { "U" }

    val totalEscaneados = escaneos.size
    val co2Total = escaneos.sumOf { it.co2Kg ?: it.producto.co2Kg }
    val productosKm0 = escaneos.count { (it.distanciaKm ?: Float.MAX_VALUE) < 100f }
    val miembroDesde = perfil?.createdAt?.let { formatearMiembroDesde(it) } ?: "—"

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Seguro que quieres cerrar sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        scope.launch {
                            SupabaseRepository.logout()
                            onLogoutClick()
                        }
                    }
                ) {
                    Text("Cerrar sesión", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showPrivacidadDialog) {
        LegalDialog(
            title = "Política de privacidad",
            body = POLITICA_PRIVACIDAD,
            onDismiss = { showPrivacidadDialog = false }
        )
    }

    if (showTerminosDialog) {
        LegalDialog(
            title = "Términos y condiciones",
            body = TERMINOS_CONDICIONES,
            onDismiss = { showTerminosDialog = false }
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
                    // Cabecera con avatar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(88.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    iniciales,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                nombre,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                email.ifBlank { "—" },
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Estadísticas rápidas
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatItem(
                            modifier = Modifier.weight(1f),
                            value = "$totalEscaneados",
                            label = "Escaneados"
                        )
                        StatItem(
                            modifier = Modifier.weight(1f),
                            value = "${"%.1f".format(co2Total)} kg",
                            label = "CO₂ total"
                        )
                        StatItem(
                            modifier = Modifier.weight(1f),
                            value = "$productosKm0",
                            label = "Km 0"
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        "Información personal",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column {
                            ProfileRow(
                                icon = Icons.Default.Person,
                                label = "Nombre",
                                value = nombre
                            )
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                            ProfileRow(
                                icon = Icons.Default.Email,
                                label = "Correo electrónico",
                                value = email.ifBlank { "—" }
                            )
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                            ProfileRow(
                                icon = Icons.Default.Star,
                                label = "Miembro desde",
                                value = miembroDesde
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        "Legal",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column {
                            LegalRow(
                                icon = Icons.Default.Lock,
                                label = "Política de privacidad",
                                onClick = { showPrivacidadDialog = true }
                            )
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                            LegalRow(
                                icon = Icons.Default.Info,
                                label = "Términos y condiciones",
                                onClick = { showTerminosDialog = true }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Button(
                        onClick = { showLogoutDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Cerrar sesión",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun LegalDialog(title: String, body: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.SemiBold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(body, fontSize = 13.sp, lineHeight = 20.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        }
    )
}

@Composable
private fun LegalRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        Text("›", fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun StatItem(modifier: Modifier = Modifier, value: String, label: String) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                label,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProfileRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

private fun formatearMiembroDesde(isoFecha: String): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val fechaStr = isoFecha.substringBefore("+").trimEnd('Z').trim()
        val fecha = sdf.parse(fechaStr) ?: return isoFecha
        SimpleDateFormat("MMMM yyyy", Locale("es", "ES"))
            .format(fecha)
            .replaceFirstChar { it.uppercaseChar() }
    } catch (e: Exception) {
        isoFecha
    }
}
