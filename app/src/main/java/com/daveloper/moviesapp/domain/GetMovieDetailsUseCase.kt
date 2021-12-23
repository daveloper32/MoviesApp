package com.daveloper.moviesapp.domain

import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.repository.MovieDetailsRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {
    suspend fun getData (
        movieId: Int,
        internetConnection: Boolean,
        refresh: Boolean = false,
        languageCode: String = "en",
        countryCode: String = "US"
    ): Movie {
        try {
            return movieDetailsRepository.getMovieDetails(movieId, internetConnection, refresh, languageCode, countryCode)
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}