package org.example.project.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.model.Movie
import org.example.project.data.repository.MovieDataSource
import org.example.project.data.repository.MovieRepositoryRouter

class MoviesViewModel(
    private val router: MovieRepositoryRouter
) : ViewModel() {

    private val formState = MoviesFormState()

    val uiState: StateFlow<MoviesUiState> = combine(
        router.source,
        router.observeMovies(),
        formState.idInput,
        formState.title,
        formState.director,
        formState.yearInput,
        formState.isBusy,
        formState.statusMessage,
        formState.errorMessage
    ) { source, movies, idInput, title, director, yearInput, isBusy, status, error ->
        MoviesUiState(
            source = source,
            movies = movies,
            idInput = idInput,
            title = title,
            director = director,
            yearInput = yearInput,
            isBusy = isBusy,
            statusMessage = status,
            errorMessage = error
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MoviesUiState())

    fun onSourceToggle(useSqlDelight: Boolean) {
        router.setSource(if (useSqlDelight) MovieDataSource.SqlDelight else MovieDataSource.Realm)
    }

    fun onIdChange(value: String) {
        formState.idInput.update { value.filter { char -> char.isDigit() } }
    }

    fun onTitleChange(value: String) {
        formState.title.update { value }
    }

    fun onDirectorChange(value: String) {
        formState.director.update { value }
    }

    fun onYearChange(value: String) {
        formState.yearInput.update { value.filter { char -> char.isDigit() } }
    }

    fun onSelectMovie(movie: Movie) {
        formState.idInput.update { movie.id?.toString().orEmpty() }
        formState.title.update { movie.title }
        formState.director.update { movie.director }
        formState.yearInput.update { movie.year.toString() }
        formState.statusMessage.update { "Pelicula seleccionada" }
        formState.errorMessage.update { null }
    }

    fun clearForm() {
        formState.clear()
    }

    fun saveMovie() {
        val title = formState.title.value.trim()
        val director = formState.director.value.trim()
        val year = formState.yearInput.value.trim().toIntOrNull()

        if (title.isEmpty() || director.isEmpty() || year == null) {
            formState.errorMessage.update { "Completa titulo, director y ano" }
            formState.statusMessage.update { null }
            return
        }

        val id = formState.idInput.value.toLongOrNull()

        viewModelScope.launch {
            formState.isBusy.update { true }
            formState.errorMessage.update { null }
            formState.statusMessage.update { null }

            runCatching {
                router.save(Movie(id = id, title = title, director = director, year = year))
            }.onSuccess {
                formState.isBusy.update { false }
                formState.statusMessage.update { "Pelicula guardada" }
            }.onFailure {
                formState.isBusy.update { false }
                formState.errorMessage.update { "Error al guardar" }
            }
        }
    }

    fun deleteMovie() {
        val id = formState.idInput.value.toLongOrNull() ?: run {
            formState.errorMessage.update { "Ingresa un ID valido" }
            formState.statusMessage.update { null }
            return
        }

        viewModelScope.launch {
            formState.isBusy.update { true }
            formState.errorMessage.update { null }
            formState.statusMessage.update { null }

            runCatching { router.delete(id) }
                .onSuccess {
                    formState.isBusy.update { false }
                    formState.statusMessage.update { "Pelicula eliminada" }
                }
                .onFailure {
                    formState.isBusy.update { false }
                    formState.errorMessage.update { "Error al eliminar" }
                }
        }
    }
}

private class MoviesFormState {
    val idInput = kotlinx.coroutines.flow.MutableStateFlow("")
    val title = kotlinx.coroutines.flow.MutableStateFlow("")
    val director = kotlinx.coroutines.flow.MutableStateFlow("")
    val yearInput = kotlinx.coroutines.flow.MutableStateFlow("")
    val isBusy = kotlinx.coroutines.flow.MutableStateFlow(false)
    val statusMessage = kotlinx.coroutines.flow.MutableStateFlow<String?>(null)
    val errorMessage = kotlinx.coroutines.flow.MutableStateFlow<String?>(null)

    fun clear() {
        idInput.update { "" }
        title.update { "" }
        director.update { "" }
        yearInput.update { "" }
        statusMessage.update { null }
        errorMessage.update { null }
    }
}

data class MoviesUiState(
    val source: MovieDataSource = MovieDataSource.SqlDelight,
    val movies: List<Movie> = emptyList(),
    val idInput: String = "",
    val title: String = "",
    val director: String = "",
    val yearInput: String = "",
    val isBusy: Boolean = false,
    val statusMessage: String? = null,
    val errorMessage: String? = null
)
