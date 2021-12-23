package com.daveloper.moviesapp.data.model.use_cases.general

import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.use_cases.room.GetMovieFromLocalDBUseCase
import com.daveloper.moviesapp.data.model.use_cases.room.UpdateMovieInLocalDBUseCase
import timber.log.Timber
import javax.inject.Inject

class AddOrRemoveUserFavoriteMoviesInLocalDBUseCase @Inject constructor(
    private val getMovieFromLocalDBUseCase: GetMovieFromLocalDBUseCase,
    private val updateMovieInLocalDBUseCase: UpdateMovieInLocalDBUseCase
) {
    suspend fun addorRemoveData (
        movieId: Int
    ): Movie {
        try {
            // get movie from local db
            val data = getMovieFromLocalDBUseCase.getData(movieId)
            // Verify id data is null
            if (data != null) {
                // Verify state of isUserFavoriteMovie
                // If is true change the value to false
                // If is false change the value to true
                data.isUserFavoriteMovie = !data.isUserFavoriteMovie
                // We need to update the Movie on db
                updateMovieInLocalDBUseCase.updateData(data)
                return data

            } else {
                Timber.e("Couldn't add or remove the movie [$movieId] to/from local database")
                throw Exception("Couldn't add or remove the movie [$movieId] to/from local database")
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}