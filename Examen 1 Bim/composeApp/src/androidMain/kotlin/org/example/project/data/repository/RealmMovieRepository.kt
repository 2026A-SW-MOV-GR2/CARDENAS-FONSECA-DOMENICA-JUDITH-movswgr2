package org.example.project.data.repository

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.example.project.data.model.Movie
import org.example.project.data.storage.realm.MovieRealmEntity

class RealmMovieRepository(
    private val realm: Realm,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository {

    override fun observeMovies(): Flow<List<Movie>> {
        return realm.query<MovieRealmEntity>()
            .asFlow()
            .map { results: ResultsChange<MovieRealmEntity> ->
                results.list.map { entity ->
                    Movie(
                        id = entity.id,
                        title = entity.title,
                        director = entity.director,
                        year = entity.year
                    )
                }
            }
    }

    override suspend fun upsert(movie: Movie) {
        val targetId = movie.id ?: nextId()
        withContext(dispatcher) {
            realm.write {
                val existing = query<MovieRealmEntity>("id == $0", targetId)
                    .first()
                    .find()
                if (existing == null) {
                    copyToRealm(
                        MovieRealmEntity().apply {
                            id = targetId
                            title = movie.title
                            director = movie.director
                            year = movie.year
                        }
                    )
                } else {
                    existing.title = movie.title
                    existing.director = movie.director
                    existing.year = movie.year
                }
            }
        }
    }

    override suspend fun deleteById(id: Long) {
        withContext(dispatcher) {
            realm.write {
                query<MovieRealmEntity>("id == $0", id)
                    .first()
                    .find()
                    ?.let { delete(it) }
            }
        }
    }

    private fun nextId(): Long {
        return System.currentTimeMillis()
    }
}
