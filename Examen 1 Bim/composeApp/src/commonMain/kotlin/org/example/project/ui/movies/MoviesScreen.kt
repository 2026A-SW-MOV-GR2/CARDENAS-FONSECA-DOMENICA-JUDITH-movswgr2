package org.example.project.ui.movies

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.data.repository.MovieDataSource
import org.example.project.data.repository.rememberMovieRepositoryRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen() {
    val router = rememberMovieRepositoryRouter()
    val viewModel: MoviesViewModel = viewModel { MoviesViewModel(router) }
    val state by viewModel.uiState.collectAsState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            TopAppBar(
                title = { Text("CRUD de Peliculas") },
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text(
                            text = if (state.source == MovieDataSource.SqlDelight) "SQL" else "NoSQL",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Switch(
                            checked = state.source == MovieDataSource.SqlDelight,
                            onCheckedChange = viewModel::onSourceToggle,
                            enabled = !state.isBusy
                        )
                    }
                }
            )

            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val chipText = if (state.source == MovieDataSource.SqlDelight) {
                    "Leyendo desde SQLDelight"
                } else {
                    "Leyendo desde Realm/NoSQL"
                }
                val chipColor = if (state.source == MovieDataSource.SqlDelight) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                } else {
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f)
                }

                FilterChip(
                    selected = true,
                    onClick = {},
                    enabled = false,
                    label = { Text(chipText) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = chipColor,
                        disabledContainerColor = chipColor,
                        labelColor = MaterialTheme.colorScheme.onSurface,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                OutlinedTextField(
                    value = state.idInput,
                    onValueChange = viewModel::onIdChange,
                    label = { Text("ID (para editar/eliminar)") },
                    enabled = !state.isBusy,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.title,
                    onValueChange = viewModel::onTitleChange,
                    label = { Text("Titulo") },
                    enabled = !state.isBusy,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.director,
                    onValueChange = viewModel::onDirectorChange,
                    label = { Text("Director") },
                    enabled = !state.isBusy,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.yearInput,
                    onValueChange = viewModel::onYearChange,
                    label = { Text("Ano") },
                    enabled = !state.isBusy,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = viewModel::saveMovie,
                        enabled = !state.isBusy
                    ) {
                        Text("Guardar")
                    }
                    Button(
                        onClick = viewModel::deleteMovie,
                        enabled = !state.isBusy
                    ) {
                        Text("Eliminar")
                    }
                    Button(
                        onClick = viewModel::clearForm,
                        enabled = !state.isBusy
                    ) {
                        Text("Limpiar")
                    }
                }

                state.statusMessage?.let { message ->
                    Text(text = message, color = MaterialTheme.colorScheme.primary)
                }

                state.errorMessage?.let { message ->
                    Text(text = message, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Peliculas registradas",
                    style = MaterialTheme.typography.titleMedium
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.movies, key = { movie -> movie.id ?: 0L }) { movie ->
                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.onSelectMovie(movie) },
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "${movie.title} (${movie.year})",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Director: ${movie.director}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "ID: ${movie.id ?: "-"}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
