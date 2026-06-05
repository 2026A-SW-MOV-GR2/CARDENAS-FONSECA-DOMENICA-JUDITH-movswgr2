package org.example.project.data.storage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

private class InMemorySecretsStorage : SecretsStorage {
    private val data = mutableMapOf<String, String>()

    override suspend fun save(key: String, value: String, engine: StorageEngine) {
        data[key] = value
    }

    override suspend fun load(key: String, engine: StorageEngine): String? {
        return data[key]
    }
}

@Composable
actual fun rememberSecretsStorage(): SecretsStorage {
    return remember { InMemorySecretsStorage() }
}
