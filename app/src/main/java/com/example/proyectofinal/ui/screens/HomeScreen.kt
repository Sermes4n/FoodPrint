package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.R
import com.example.proyectofinal.data.SupabaseRepository
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(onEscanearClick: () -> Unit) {

    var escaneadosHoy by remember { mutableStateOf(0) }
    var co2Hoy        by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        try {
            val (count, co2) = SupabaseRepository.getStatsHoy()
            escaneadosHoy = count
            co2Hoy        = co2
        } catch (_: Exception) { }
    }

    val nombre = remember {
        val email = SupabaseRepository.getUsuarioEmail() ?: ""
        email.substringBefore("@").replaceFirstChar { it.uppercaseChar() }
    }

    val hoy = remember {
        SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "ES"))
            .format(Date())
            .replaceFirstChar { it.uppercaseChar() }
    }

    val primary = MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ── Hero ────────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(primary, primary.copy(alpha = 0.82f))
                    )
                )
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(top = 18.dp, bottom = 24.dp)
        ) {
            Column {
                Image(
                    painter            = painterResource(id = R.drawable.icono_main),
                    contentDescription = "FoodPrint",
                    modifier           = Modifier.height(32.dp)
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text       = "Hola, $nombre",
                    fontSize   = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White
                )
                Text(
                    text     = hoy,
                    fontSize = 12.sp,
                    color    = Color.White.copy(alpha = 0.70f)
                )
            }
        }

        // ── Content ─────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Stats label
            Text(
                text          = "TU IMPACTO HOY",
                style         = MaterialTheme.typography.labelSmall,
                color         = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Metric cards row
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text  = "CO₂",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.65f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text       = "${"%.1f".format(co2Hoy)} kg",
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color      = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text  = "emitidos hoy",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape    = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text  = "PRODUCTOS",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text       = "$escaneadosHoy",
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color      = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text  = "escaneados hoy",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Scan square button ───────────────────────────────────────────
            Button(
                onClick  = onEscanearClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                shape  = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(
                    modifier            = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector        = Icons.Default.Add,
                        contentDescription = null,
                        modifier           = Modifier.size(72.dp),
                        tint               = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text       = "Escanear producto",
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text     = "Toca para escanear",
                        fontSize = 13.sp,
                        color    = Color.White.copy(alpha = 0.65f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
