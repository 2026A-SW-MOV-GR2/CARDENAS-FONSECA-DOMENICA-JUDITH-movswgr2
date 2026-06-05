package org.example.project.data.storage

import androidx.compose.runtime.Composable

enum class StorageEngine {
    SharedPreferences,
    DataStore,
    EncryptedSharedPreferences
}

interface SecretsStorage {
    suspend fun save(key: String, value: String, engine: StorageEngine)
    suspend fun load(key: String, engine: StorageEngine): String?
}

@Composable
expect fun rememberSecretsStorage(): SecretsStorage
