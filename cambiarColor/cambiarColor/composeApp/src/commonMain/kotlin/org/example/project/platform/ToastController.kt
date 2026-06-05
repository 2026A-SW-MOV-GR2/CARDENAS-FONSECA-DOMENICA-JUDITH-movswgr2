package org.example.project.platform

import androidx.compose.runtime.Composable

interface ToastController {
    fun show(message: String)
}

@Composable
expect fun rememberToastController(): ToastController
