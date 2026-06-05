package org.example.project.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.example.project.data.model.Movie
import org.example.project.data.storage.sqldelight.MoviesDatabase

class SqlDelightMovieRepository(
    private val database: MoviesDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository {

    private val queries = database.moviesQueries

    override fun observeMovies(): Flow<List<Movie>> {
        return queries.selectAll()
            .asFlow()
            .mapToList(dispatcher)
            .map { entities ->
                entities.map { entity ->
                    Movie(
                        id = entity.id,
                        title = entity.title,
                        director = entity.director,
                        year = entity.year.toInt()
                    )
                }
            }
    }

    override suspend fun upsert(movie: Movie) {
        withContext(dispatcher) {
            if (movie.id == null) {
                queries.insertMovie(
                    title = movie.title,
                    director = movie.director,
                    year = movie.year.toLong()
                )
            } else {
                queries.updateMovie(
                    title = movie.title,
                    director = movie.director,
                    year = movie.year.toLong(),
                    id = movie.id
                )
            }
        }
    }

    override suspend fun deleteById(id: Long) {
        withContext(dispatcher) {
            queries.deleteMovie(id)
        }
    }
}
