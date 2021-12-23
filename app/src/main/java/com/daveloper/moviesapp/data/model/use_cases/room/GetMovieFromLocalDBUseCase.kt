package com.daveloper.moviesapp.data.model.use_cases.room

import com.daveloper.moviesapp.data.local_database.room.MovieDao
import com.daveloper.moviesapp.data.model.entity.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class GetMovieFromLocalDBUseCase @Inject constructor(
    private val dao: MovieDao
) {
    suspend fun getData (
        movieID: Int
    ): Movie? {
        return withContext(Dispatchers.IO) {
            try {
                val data = dao.getMovie(movieID)
                if (data != null) {
                    data
                } else {
                    Timber.e("GetMovieFromLocalDBUseCase couldn't found any value")
                    data
                }
            } catch (e: Exception) {
                throw Exception(e)
            }
        }
    }
}