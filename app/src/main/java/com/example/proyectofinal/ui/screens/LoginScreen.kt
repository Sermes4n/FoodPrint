package com.example.proyectofinal.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectofinal.R
import com.example.proyectofinal.data.SupabaseRepository
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginClick: () -> Unit, onRegisterClick: () -> Unit) {

    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.icono_log),
            contentDescription = "Logo FoodPrint",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it; error = null },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !cargando
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it; error = null },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            enabled = !cargando
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = {
                if (correo.isBlank()) {
                    error = "Introduce tu correo electrónico para recuperar la contraseña."
                    return@TextButton
                }
                scope.launch {
                    cargando = true
                    error = null
                    try {
                        SupabaseRepository.resetPassword(correo.trim())
                        error = "Correo de recuperación enviado a $correo"
                    } catch (e: Exception) {
                        error = e.message
                    } finally {
                        cargando = false
                    }
                }
            },
            modifier = Modifier.align(Alignment.End),
            enabled = !cargando
        ) {
            Text("¿Olvidaste la contraseña?")
        }

        if (error != null) {
            Spacer(modifier = Modifier.height(4.dp))
            val esExito = error!!.startsWith("Correo de recuperación")
            Text(
                text = error!!,
                color = if (esExito) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (correo.isBlank() || contrasena.isBlank()) {
                    error = "Completa todos los campos."
                    return@Button
                }
                scope.launch {
                    cargando = true
                    error = null
                    try {
                        SupabaseRepository.login(correo.trim(), contrasena)
                        onLoginClick()
                    } catch (e: Exception) {
                        error = e.message
                    } finally {
                        cargando = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.medium,
            enabled = !cargando
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Iniciar sesión", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.medium,
            enabled = !cargando
        ) {
            Text("Registrarse", fontSize = 16.sp)
        }
    }
}
