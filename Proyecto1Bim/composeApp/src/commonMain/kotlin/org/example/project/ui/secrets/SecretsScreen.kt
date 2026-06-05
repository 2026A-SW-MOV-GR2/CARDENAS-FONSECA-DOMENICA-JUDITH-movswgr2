package org.example.project.ui.secrets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import org.example.project.data.storage.StorageEngine
import org.example.project.data.storage.rememberSecretsStorage

@Composable
fun SecretsScreen() {
    val storage = rememberSecretsStorage()
    val viewModel: SecretsViewModel = viewModel { SecretsViewModel(storage) }
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is SecretsEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = MaterialTheme.colorScheme.surface
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Preferencias de Series",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = state.key,
                    onValueChange = viewModel::onKeyChange,
                    label = { Text("Clave de la serie") },
                    enabled = !state.isBusy,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.value,
                    onValueChange = viewModel::onValueChange,
                    label = { Text("Valor") },
                    enabled = !state.isBusy,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Almacenamiento de preferencias",
                    style = MaterialTheme.typography.bodyMedium
                )

                StorageEngine.values().forEach { engine ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RadioButton(
                            selected = state.engine == engine,
                            onClick = { viewModel.onEngineChange(engine) },
                            enabled = !state.isBusy
                        )
                        Text(text = engine.name, style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = viewModel::save,
                        enabled = !state.isBusy
                    ) {
                        Text("Guardar preferencia")
                    }
                    Button(
                        onClick = viewModel::load,
                        enabled = !state.isBusy
                    ) {
                        Text("Cargar preferencia")
                    }
                }

                state.statusMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
