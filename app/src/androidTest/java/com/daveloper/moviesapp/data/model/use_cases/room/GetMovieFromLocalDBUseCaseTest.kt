package com.daveloper.moviesapp.data.model.use_cases.room

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.daveloper.moviesapp.core.RoomProvider
import com.daveloper.moviesapp.data.local_database.room.MovieDao
import com.daveloper.moviesapp.data.local_database.room.RoomMovieDatabase
import com.daveloper.moviesapp.data.model.entity.Movie
import com.google.common.truth.Truth.assertThat
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
class GetMovieFromLocalDBUseCaseTest {

    private val dispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(dispatcher)

    lateinit var db: RoomMovieDatabase

    lateinit var dao: MovieDao

    @Inject
    lateinit var insertMovieInLocalDBUseCase: InsertMovieInLocalDBUseCase

    @Inject
    lateinit var getMovieFromLocalDBUseCase: GetMovieFromLocalDBUseCase

    @Before
    fun setup(){
        db = RoomProvider.moviesDatabase(ApplicationProvider.getApplicationContext())
        dao = RoomProvider.movieDao(db)
        insertMovieInLocalDBUseCase = InsertMovieInLocalDBUseCase(dao)
        getMovieFromLocalDBUseCase = GetMovieFromLocalDBUseCase(dao)
    }

    @After
    fun teardown(){
        db.close()
    }

    private val movie = Movie(
        1
    )

    private val movie2 = Movie(
        2
    )

    @Test
    fun getAMovie ()  {
        testScope.launch {
            // Insert a movie
            insertMovieInLocalDBUseCase.insertData(movie)
            // Get the movie (id = 1)
            val result = getMovieFromLocalDBUseCase.getData(1)
            // Assert
            assertThat(result).isEqualTo(movie)
            assertThat(result).isNotEqualTo(movie2)
        }
    }
}