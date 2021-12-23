package com.daveloper.moviesapp.data.model.repository

import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.use_cases.general.AddOrRemoveUserFavoriteMoviesInLocalDBUseCase
import com.daveloper.moviesapp.data.model.use_cases.general.GetAllDetailsOfAMovieFromRepositoryUseCase
import javax.inject.Inject

class MovieDetailsRepository @Inject constructor(
    private val getAllDetailsOfAMovieFromRepositoryUseCase: GetAllDetailsOfAMovieFromRepositoryUseCase,
    private val addOrRemoveUserFavoriteMoviesInLocalDBUseCase: AddOrRemoveUserFavoriteMoviesInLocalDBUseCase
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

    suspend fun addOrRemoveMovieOnUserFavoriteMovies (
        movieId: Int
    ) {
        try {
            addOrRemoveUserFavoriteMoviesInLocalDBUseCase.addorRemoveData(movieId)
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}