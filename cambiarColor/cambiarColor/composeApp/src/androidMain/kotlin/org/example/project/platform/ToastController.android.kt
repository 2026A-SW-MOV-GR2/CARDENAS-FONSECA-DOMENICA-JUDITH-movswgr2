package org.example.project.platform

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberToastController(): ToastController {
    val context = LocalContext.current
    return remember(context) {
        object : ToastController {
            override fun show(message: String) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
