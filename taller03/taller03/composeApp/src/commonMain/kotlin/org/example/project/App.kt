package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    MaterialTheme {
        // Usamos el proveedor de recursos que resuelve la implementación nativa
        val textoActual = AppResources.getLabel()
        val colorTexto = AppResources.getTextColor()
        val fondoColor = AppResources.getBackgroundColor()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoColor),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = textoActual,
                    color = colorTexto,
                    style = MaterialTheme.typography.headlineLarge
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Demostración de persistencia de estado ante cambios de configuración
                // Gracias a android:configChanges en el Manifest, la Activity no se recrea
                // y este estado se mantiene intacto.
                var contador by remember { mutableStateOf(0) }
                Button(onClick = { contador++ }) {
                    Text("Persistencia de estado (Contador): $contador")
                }
            }
        }
    }
}
