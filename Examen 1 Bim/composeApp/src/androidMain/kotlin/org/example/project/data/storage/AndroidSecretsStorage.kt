package org.example.project.data.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class AndroidSecretsStorage(private val context: Context) : SecretsStorage {
    private val sharedPrefs = context.getSharedPreferences("secrets_prefs", Context.MODE_PRIVATE)

    private val encryptedPrefs by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "encrypted_secrets",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override suspend fun save(key: String, value: String, engine: StorageEngine) {
        withContext(Dispatchers.IO) {
            when (engine) {
                StorageEngine.SharedPreferences -> {
                    sharedPrefs.edit().putString(key, value).commit()
                }
                StorageEngine.DataStore -> {
                    context.secretsDataStore.edit { prefs ->
                        prefs[stringPreferencesKey(key)] = value
                    }
                }
                StorageEngine.EncryptedSharedPreferences -> {
                    encryptedPrefs.edit().putString(key, value).commit()
                }
            }
        }
    }

    override suspend fun load(key: String, engine: StorageEngine): String? {
        return withContext(Dispatchers.IO) {
            when (engine) {
                StorageEngine.SharedPreferences -> sharedPrefs.getString(key, null)
                StorageEngine.DataStore -> {
                    val prefs = context.secretsDataStore.data.first()
                    prefs[stringPreferencesKey(key)]
                }
                StorageEngine.EncryptedSharedPreferences -> encryptedPrefs.getString(key, null)
            }
        }
    }
}
