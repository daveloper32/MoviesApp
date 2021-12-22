package com.daveloper.moviesapp.data.model.repository

import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.use_cases.general.GetPopularMoviesFromRepositoryUseCase
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val getPopularMoviesFromRepositoryUseCase: GetPopularMoviesFromRepositoryUseCase
) {
    suspend fun getPopularMovies (
        internetConnection: Boolean
    ) : List<Movie> {
        try {
            return getPopularMoviesFromRepositoryUseCase.getData(internetConnection)
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}