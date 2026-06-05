package org.example.project.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.example.project.data.InMemoryLibrary
import org.example.project.platform.rememberToastController
import org.example.project.ui.screens.LibraryFormScreen
import org.example.project.ui.screens.LibraryListScreen

@Composable
fun LibraryApp() {
    MaterialTheme {
        val library = remember { InMemoryLibrary() }
        val toast = rememberToastController()

        var screen: Screen by remember { mutableStateOf(Screen.List) }

        when (val current = screen) {
            Screen.List -> {
                LibraryListScreen(
                    books = library.books,
                    onAddBook = { screen = Screen.Form(bookId = null) },
                    onEditBook = { id -> screen = Screen.Form(bookId = id) },
                    onDeleteBook = { id ->
                        library.delete(id)
                        toast.show("Libro eliminado")
                    },
                )
            }

            is Screen.Form -> {
                val editingBook = current.bookId?.let(library::findById)
                LibraryFormScreen(
                    initial = editingBook,
                    onBack = { screen = Screen.List },
                    onSave = { updated ->
                        if (editingBook == null) {
                            val nextId = (library.books.maxOfOrNull { it.id } ?: 0L) + 1L
                            library.add(updated.copy(id = nextId))
                            toast.show("Libro agregado")
                        } else {
                            library.update(updated)
                            toast.show("Cambios guardados")
                        }
                        screen = Screen.List
                    },
                )
            }
        }
    }
}
