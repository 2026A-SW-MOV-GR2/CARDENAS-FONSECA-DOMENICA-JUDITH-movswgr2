package org.example.project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.model.Book
import org.example.project.ui.components.BookListItem
import org.example.project.ui.components.ConfirmDeleteDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryListScreen(
    books: SnapshotStateList<Book>,
    onAddBook: () -> Unit,
    onEditBook: (Long) -> Unit,
    onDeleteBook: (Long) -> Unit,
) {
    var pendingDelete: Book? by remember { mutableStateOf(null) }

    if (pendingDelete != null) {
        ConfirmDeleteDialog(
            title = pendingDelete!!.title,
            onConfirm = {
                onDeleteBook(pendingDelete!!.id)
                pendingDelete = null
            },
            onDismiss = { pendingDelete = null },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Mi Biblioteca") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddBook) {
                Text(text = "+")
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                items = books,
                key = { it.id },
            ) { book ->
                BookListItem(
                    book = book,
                    onClick = { onEditBook(book.id) },
                    onLongPress = { pendingDelete = book },
                    onDeleteClick = { pendingDelete = book },
                )
            }
        }
    }
}
