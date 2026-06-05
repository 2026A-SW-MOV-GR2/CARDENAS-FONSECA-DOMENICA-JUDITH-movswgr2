package org.example.project.ui.rest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.data.network.createHttpClient
import org.example.project.data.repository.PostRepository

@Composable
fun RestPostScreen() {
    val repository = remember { PostRepository(createHttpClient()) }
    val viewModel: PostViewModel = viewModel { PostViewModel(repository) }
    val state by viewModel.uiState.collectAsState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Modulo 1 - Catalogo de Series",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = state.postIdInput,
                onValueChange = viewModel::onPostIdChange,
                label = { Text("ID de serie") },
                enabled = !state.isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = viewModel::fetchPost,
                    enabled = !state.isLoading
                ) {
                    Text("Buscar")
                }
                Button(
                    onClick = viewModel::updatePost,
                    enabled = !state.isLoading
                ) {
                    Text("Actualizar")
                }
            }

            OutlinedTextField(
                value = state.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text("Titulo de serie") },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.body,
                onValueChange = viewModel::onBodyChange,
                label = { Text("Resumen") },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            if (state.isLoading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.statusMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            state.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
