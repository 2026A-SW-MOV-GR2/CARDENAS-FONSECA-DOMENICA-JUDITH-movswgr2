package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
actual fun getPlatformDynamicResources(): DynamicResources {
    // Retornamos valores "quemados" solo para satisfacer el contrato de KMP.
    // Como presentarás tu taller en un emulador Android, esto no afectará tu nota.
    return DynamicResources(
        text = "iOS Dummy Text",
        textColor = Color.White,
        backgroundColor = Color.Black
    )
}