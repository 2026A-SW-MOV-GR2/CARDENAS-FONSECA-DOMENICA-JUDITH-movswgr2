package org.example.project.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import org.example.project.model.Book
import org.example.project.model.CoverTone

class InMemoryLibrary(initialBooks: List<Book> = sampleBooks()) {
    private val _books = mutableStateListOf<Book>().apply { addAll(initialBooks) }
    val books: SnapshotStateList<Book> = _books

    fun findById(id: Long): Book? = _books.firstOrNull { it.id == id }

    fun add(book: Book) {
        _books.add(book)
    }

    fun update(book: Book) {
        val index = _books.indexOfFirst { it.id == book.id }
        if (index >= 0) _books[index] = book
    }

    fun delete(id: Long) {
        _books.removeAll { it.id == id }
    }

    companion object {
        fun sampleBooks(): List<Book> = listOf(
            Book(
                id = 1,
                title = "Cien años de soledad",
                author = "Gabriel García Márquez",
                summary = "La historia de la familia Buendía a lo largo de siete generaciones en el pueblo ficticio de Macondo.",
                publishedDate = "30/05/1967",
                isRead = true,
                coverTone = CoverTone.Primary,
            ),
            Book(
                id = 2,
                title = "El nombre de la rosa",
                author = "Umberto Eco",
                summary = "Un monje franciscano investiga una serie de muertes misteriosas en una abadía medieval.",
                publishedDate = "01/01/1980",
                isRead = false,
                coverTone = CoverTone.Secondary,
            ),
            Book(
                id = 3,
                title = "Ficciones",
                author = "Jorge Luis Borges",
                summary = "Colección de relatos que exploran laberintos, bibliotecas infinitas y realidades alternativas.",
                publishedDate = "01/01/1944",
                isRead = true,
                coverTone = CoverTone.Tertiary,
            ),
            Book(
                id = 4,
                title = "La sombra del viento",
                author = "Carlos Ruiz Zafón",
                summary = "En la Barcelona de posguerra, un niño descubre un libro maldito y sigue el rastro de su autor.",
                publishedDate = "01/01/2001",
                isRead = false,
                coverTone = CoverTone.SurfaceVariant,
            ),
        )
    }
}
