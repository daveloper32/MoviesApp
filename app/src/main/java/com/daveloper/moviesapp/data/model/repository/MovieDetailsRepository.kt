package com.daveloper.moviesapp.data.model.repository

import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.use_cases.general.GetAllDetailsOfAMovieFromRepositoryUseCase
import javax.inject.Inject

class MovieDetailsRepository @Inject constructor(
    private val getAllDetailsOfAMovieFromRepositoryUseCase: GetAllDetailsOfAMovieFromRepositoryUseCase
) {

    suspend fun getMovieDetails (
        movieId: Int,
        internetConnection: Boolean,
        refresh: Boolean = false,
        languageCode: String = "en",
        countryCode: String = "US"
    ) : Movie {
        try {
            return getAllDetailsOfAMovieFromRepositoryUseCase.getData(movieId, internetConnection, refresh, languageCode, countryCode)
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}