package org.example.project.data.repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.example.project.data.logging.AppLogger
import org.example.project.data.storage.realm.MovieRealmEntity
import org.example.project.data.storage.sqldelight.MoviesDatabase

@Composable
actual fun rememberMovieRepositoryRouter(): MovieRepositoryRouter {
    val context = LocalContext.current

    return remember(context) {
        val driver = AndroidSqliteDriver(
            schema = MoviesDatabase.Schema,
            context = context,
            name = "movies.db"
        )
        val database = MoviesDatabase(driver)

        val realmConfig = RealmConfiguration.Builder(
            schema = setOf(MovieRealmEntity::class)
        )
            .name("movies.realm")
            .build()
        val realm = Realm.open(realmConfig)

        MovieRepositoryRouter(
            sqlDelightRepository = SqlDelightMovieRepository(database),
            realmRepository = RealmMovieRepository(realm),
            logger = AppLogger
        )
    }
}
