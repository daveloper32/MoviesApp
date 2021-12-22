package com.daveloper.moviesapp.data.model.use_cases.room

import com.daveloper.moviesapp.data.local_database.room.MovieDao
import com.daveloper.moviesapp.data.model.entity.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class GetUpcomingMoviesFromLocalDBUseCase @Inject constructor(
    private val dao: MovieDao
) {
    suspend fun getData (

    ): List<Movie> {
        return withContext(Dispatchers.IO) {
            try {
                val data = dao.getUpcomingMovies()
                if (!data.isNullOrEmpty()){
                    data
                } else {
                    Timber.e("GetUpcomingMoviesFromLocalDBUseCase couldn't found any value")
                    emptyList()
                }

            } catch (e: Exception) {
                throw Exception("GetUpcomingMoviesFromLocalDBUseCase couldn't found any value")
            }
        }
    }
}