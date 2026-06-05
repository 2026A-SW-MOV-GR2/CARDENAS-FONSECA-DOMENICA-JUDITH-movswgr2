package org.example.project.data.storage

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.secretsDataStore by preferencesDataStore(name = "secrets_datastore")
