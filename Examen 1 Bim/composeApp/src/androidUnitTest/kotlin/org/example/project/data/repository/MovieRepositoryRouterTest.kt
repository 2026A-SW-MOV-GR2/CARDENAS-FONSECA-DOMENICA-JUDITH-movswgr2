package org.example.project.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.example.project.data.logging.Logger
import org.example.project.data.model.Movie
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryRouterTest {

    @Test
    fun writeGoesToSelectedRepository() = runTest {
        val sqlRepo = FakeMovieRepository()
        val realmRepo = FakeMovieRepository()
        val router = MovieRepositoryRouter(sqlRepo, realmRepo, FakeLogger())

        router.save(Movie(title = "Inception", director = "Nolan", year = 2010))

        assertEquals(1, sqlRepo.current().size)
        assertEquals(0, realmRepo.current().size)

        router.setSource(MovieDataSource.Realm)
        router.save(Movie(title = "Matrix", director = "Wachowski", year = 1999))

        assertEquals(1, sqlRepo.current().size)
        assertEquals(1, realmRepo.current().size)
    }

    @Test
    fun switchingUpdatesObservedList() = runTest {
        val sqlRepo = FakeMovieRepository(
            initial = listOf(Movie(id = 1L, title = "Arrival", director = "Villeneuve", year = 2016))
        )
        val realmRepo = FakeMovieRepository(
            initial = listOf(Movie(id = 10L, title = "Coco", director = "Unkrich", year = 2017))
        )
        val router = MovieRepositoryRouter(sqlRepo, realmRepo, FakeLogger())

        val emissions = mutableListOf<List<Movie>>()
        val job = launch {
            router.observeMovies()
                .take(2)
                .toList(emissions)
        }

        advanceUntilIdle()
        router.setSource(MovieDataSource.Realm)
        advanceUntilIdle()
        job.join()

        assertEquals(listOf(1L), emissions[0].map { it.id })
        assertEquals(listOf(10L), emissions[1].map { it.id })
    }
}

private class FakeMovieRepository(
    initial: List<Movie> = emptyList()
) : MovieRepository {
    private val state = MutableStateFlow(initial)

    override fun observeMovies(): Flow<List<Movie>> = state

    override suspend fun upsert(movie: Movie) {
        val id = movie.id ?: nextId()
        val updated = state.value.filterNot { it.id == id } + movie.copy(id = id)
        state.value = updated
    }

    override suspend fun deleteById(id: Long) {
        state.value = state.value.filterNot { it.id == id }
    }

    fun current(): List<Movie> = state.value

    private fun nextId(): Long {
        return (state.value.maxOfOrNull { it.id ?: 0L } ?: 0L) + 1L
    }
}

private class FakeLogger : Logger {
    override fun debug(tag: String, message: String) = Unit
    override fun info(tag: String, message: String) = Unit
    override fun error(tag: String, message: String, throwable: Throwable?) = Unit
}
