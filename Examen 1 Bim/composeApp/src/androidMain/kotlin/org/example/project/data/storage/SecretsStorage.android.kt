package org.example.project.data.storage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberSecretsStorage(): SecretsStorage {
    val context = LocalContext.current
    return remember(context) { AndroidSecretsStorage(context.applicationContext) }
}
