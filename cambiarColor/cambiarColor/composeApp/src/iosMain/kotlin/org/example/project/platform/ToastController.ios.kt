package org.example.project.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSLog

@Composable
actual fun rememberToastController(): ToastController {
    return remember {
        object : ToastController {
            override fun show(message: String) {
                NSLog(message)
            }
        }
    }
}
