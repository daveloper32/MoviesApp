package com.daveloper.moviesapp.data.model.use_cases.room

import com.daveloper.moviesapp.data.local_database.room.MovieDao
import com.daveloper.moviesapp.data.model.entity.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class InsertMovieInLocalDBUseCase @Inject constructor(
    private val dao: MovieDao
) {
    suspend fun insertData (
        newMovie: Movie
    ) {
        withContext(Dispatchers.IO) {
            try {
                dao.insertMovie(newMovie)
            } catch (e: Exception) {
                Timber.e("InsertMovieInLocalDBUseCase couldn't insert the Movie object")
                throw Exception(e)
            }
        }
    }
}