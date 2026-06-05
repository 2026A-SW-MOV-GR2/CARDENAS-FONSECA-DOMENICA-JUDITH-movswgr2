package org.example.project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import org.example.project.model.Book
import org.example.project.model.CoverTone
import org.example.project.ui.components.coverToneColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryFormScreen(
    initial: Book?,
    onBack: () -> Unit,
    onSave: (Book) -> Unit,
) {
    var title by remember(initial?.id) { mutableStateOf(initial?.title.orEmpty()) }
    var author by remember(initial?.id) { mutableStateOf(initial?.author.orEmpty()) }
    var summary by remember(initial?.id) { mutableStateOf(initial?.summary.orEmpty()) }
    var publishedDate by remember(initial?.id) { mutableStateOf(initial?.publishedDate.orEmpty()) }
    var isRead by remember(initial?.id) { mutableStateOf(initial?.isRead ?: false) }
    var coverTone by remember(initial?.id) { mutableStateOf(initial?.coverTone ?: CoverTone.Primary) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (initial == null) "Nuevo libro" else "Editar libro") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text(text = "Atrás") }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(text = "Título") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            OutlinedTextField(
                value = author,
                onValueChange = { author = it },
                label = { Text(text = "Autor") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = publishedDate,
                onValueChange = { publishedDate = it },
                label = { Text(text = "Fecha de publicación") },
                placeholder = { Text(text = "dd/mm/aaaa") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = summary,
                onValueChange = { summary = it },
                label = { Text(text = "Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 6,
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = "Color de portada", style = MaterialTheme.typography.labelLarge)
                CoverTonePicker(
                    selected = coverTone,
                    onSelect = { coverTone = it },
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(text = "¿Lo leíste?", style = MaterialTheme.typography.labelLarge)
                    Text(
                        text = if (isRead) "Leído" else "Pendiente",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.alpha(0.75f),
                    )
                }
                Switch(checked = isRead, onCheckedChange = { isRead = it })
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = {
                    onSave(
                        Book(
                            id = initial?.id ?: 0,
                            title = title.trim(),
                            author = author.trim(),
                            summary = summary.trim(),
                            publishedDate = publishedDate.trim(),
                            isRead = isRead,
                            coverTone = coverTone,
                        ),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = title.isNotBlank() && author.isNotBlank(),
            ) {
                Text(text = "Guardar cambios")
            }
        }
    }
}

@Composable
private fun CoverTonePicker(
    selected: CoverTone,
    onSelect: (CoverTone) -> Unit,
) {
    val tones = listOf(
        CoverTone.Primary,
        CoverTone.Secondary,
        CoverTone.Tertiary,
        CoverTone.Error,
        CoverTone.SurfaceVariant,
    )

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        tones.forEach { tone ->
            val color = coverToneColor(tone)
            val isSelected = tone == selected

            Spacer(
                modifier = Modifier
                    .size(if (isSelected) 20.dp else 18.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable { onSelect(tone) },
            )
        }
    }
}
