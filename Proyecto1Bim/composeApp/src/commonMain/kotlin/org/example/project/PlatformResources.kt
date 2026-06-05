package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Modelo de datos para manejar el texto, color de texto y color de fondo
data class DynamicResources(
    val text: String,
    val textColor: Color,
    val backgroundColor: Color
)

// Contrato que obliga a cada plataforma a entregarnos estos recursos
@Composable
expect fun getPlatformDynamicResources(): DynamicResources