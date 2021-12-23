package com.daveloper.moviesapp.data.model.use_cases.room

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.daveloper.moviesapp.core.RoomProvider
import com.daveloper.moviesapp.data.local_database.room.MovieDao
import com.daveloper.moviesapp.data.local_database.room.RoomMovieDatabase
import com.daveloper.moviesapp.data.model.entity.Movie
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class UpdateMovieInLocalDBUseCaseTest {

    private val dispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(dispatcher)

    lateinit var db: RoomMovieDatabase

    lateinit var dao: MovieDao

    @Inject
    lateinit var insertMovieInLocalDBUseCase: InsertMovieInLocalDBUseCase

    @Inject
    lateinit var getMovieFromLocalDBUseCase: GetMovieFromLocalDBUseCase

    @Inject
    lateinit var updateMovieInLocalDBUseCase: UpdateMovieInLocalDBUseCase

    @Before
    fun setUp() {
        db = RoomProvider.moviesDatabase(ApplicationProvider.getApplicationContext())
        dao = RoomProvider.movieDao(db)
        insertMovieInLocalDBUseCase = InsertMovieInLocalDBUseCase(dao)
        getMovieFromLocalDBUseCase = GetMovieFromLocalDBUseCase(dao)
        updateMovieInLocalDBUseCase = UpdateMovieInLocalDBUseCase(dao)
    }

    @After
    fun tearDown() {
        db.close()
    }

    private val movie = Movie(
        1
    )

    private val movie2 = Movie(
        2
    )

    @Test
    fun updateAMovie() {
        testScope.launch {
            // Insert one Movie
            insertMovieInLocalDBUseCase.insertData(movie)
            // Get the movie (id = 1)
            val result1 = getMovieFromLocalDBUseCase.getData(1)
            // Assert
            Truth.assertThat(result1).isEqualTo(movie)
            // Modify some properties of the movie
            movie.name = "Harry Potter"
            movie.tagline = "You are a magician Harry"
            // Update
            updateMovieInLocalDBUseCase.updateData(movie)
            // Get the movie (id = 1)
            val result2 = getMovieFromLocalDBUseCase.getData(1)
            // Assert
            Truth.assertThat(movie).isEqualTo(result2)
            Truth.assertThat(result2).isNotEqualTo(result2)
            if (result1 != null && result2 != null) {
                Truth.assertThat(result1.id).isEqualTo(result2.id)
            }
            Truth.assertThat(movie.name).isEqualTo("Harry Potter")
            Truth.assertThat(movie.tagline).isEqualTo("You are a magician Harry")
        }
    }
}