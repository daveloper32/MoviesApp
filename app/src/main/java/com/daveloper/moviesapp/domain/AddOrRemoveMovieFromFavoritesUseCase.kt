package com.daveloper.moviesapp.domain

import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.repository.MovieDetailsRepository
import javax.inject.Inject

class AddOrRemoveMovieFromFavoritesUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {
    suspend fun addOrRemove (
        movieId: Int
    ): Movie {
        try {
            return movieDetailsRepository.addOrRemoveMovieOnUserFavoriteMovies(movieId)
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}