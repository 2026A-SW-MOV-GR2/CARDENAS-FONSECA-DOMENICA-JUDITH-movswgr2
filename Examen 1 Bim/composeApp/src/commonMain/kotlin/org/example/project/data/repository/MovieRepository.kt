package org.example.project.data.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.data.model.Movie

interface MovieRepository {
    fun observeMovies(): Flow<List<Movie>>
    suspend fun upsert(movie: Movie)
    suspend fun deleteById(id: Long)
}
