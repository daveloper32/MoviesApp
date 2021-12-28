package com.daveloper.moviesapp.domain

import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.repository.MovieDetailsRepository
import javax.inject.Inject

class GetSimilarMoviesUseCase @Inject constructor(
    private val repository: MovieDetailsRepository
) {
    suspend fun getData (
        movieId: Int,
        similarMoviesIds: List<Int>,
        internetConnection: Boolean,
        refresh: Boolean = false,
        languageCode: String = "en",
        countryCode: String = "US"
    ): List<Movie> {
        try {
            return repository.getListOfMoviesFromMovieIds(movieId, similarMoviesIds, internetConnection, refresh, languageCode, countryCode)
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}