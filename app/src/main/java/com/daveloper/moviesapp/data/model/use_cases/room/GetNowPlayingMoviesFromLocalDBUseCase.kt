package com.daveloper.moviesapp.data.model.use_cases.room

import com.daveloper.moviesapp.data.local_database.room.MovieDao
import com.daveloper.moviesapp.data.model.entity.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class GetNowPlayingMoviesFromLocalDBUseCase @Inject constructor(
    private val dao: MovieDao
) {
    suspend fun getData (

    ): List<Movie> {
        return withContext(Dispatchers.IO) {
            try {
                val data = dao.getNowPlayingMovies()
                if (!data.isNullOrEmpty()){
                    data
                } else {
                    Timber.e("GetNowPlayingMoviesFromLocalDBUseCase couldn't found any value")
                    emptyList()
                }

            } catch (e: Exception) {
                throw Exception("GetNowPlayingMoviesFromLocalDBUseCase couldn't found any value")
            }
        }
    }
}