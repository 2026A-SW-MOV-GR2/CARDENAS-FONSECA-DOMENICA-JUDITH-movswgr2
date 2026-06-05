package org.example.project.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import org.example.project.data.logging.Logger
import org.example.project.data.model.Movie

class MovieRepositoryRouter(
    private val sqlDelightRepository: MovieRepository,
    private val realmRepository: MovieRepository,
    private val logger: Logger,
    initialSource: MovieDataSource = MovieDataSource.SqlDelight
) {
    private val _source = MutableStateFlow(initialSource)
    val source: StateFlow<MovieDataSource> = _source.asStateFlow()

    fun setSource(newSource: MovieDataSource) {
        if (newSource == _source.value) return
        logger.info(TAG, "INFO Cambio de motor a ${newSource.name}")
        _source.value = newSource
    }

    fun observeMovies(): Flow<List<Movie>> {
        return source.flatMapLatest { repositoryFor(it).observeMovies() }
    }

    suspend fun save(movie: Movie) {
        logger.debug(TAG, "DEBUG Guardando pelicula en ${source.value.name}")
        try {
            repositoryFor(source.value).upsert(movie)
        } catch (error: Throwable) {
            logger.error(TAG, "ERROR al guardar pelicula", error)
            throw error
        }
    }

    suspend fun delete(id: Long) {
        logger.debug(TAG, "DEBUG Eliminando pelicula en ${source.value.name}")
        try {
            repositoryFor(source.value).deleteById(id)
        } catch (error: Throwable) {
            logger.error(TAG, "ERROR al eliminar pelicula", error)
            throw error
        }
    }

    private fun repositoryFor(source: MovieDataSource): MovieRepository {
        return when (source) {
            MovieDataSource.SqlDelight -> sqlDelightRepository
            MovieDataSource.Realm -> realmRepository
        }
    }

    private companion object {
        const val TAG = "MovieRepository"
    }
}
