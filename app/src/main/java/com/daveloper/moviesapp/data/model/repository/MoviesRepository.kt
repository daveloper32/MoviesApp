package com.daveloper.moviesapp.data.model.repository

import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.use_cases.general.GetNowPlayingMoviesFromRepositoryUseCase
import com.daveloper.moviesapp.data.model.use_cases.general.GetPopularMoviesFromRepositoryUseCase
import com.daveloper.moviesapp.data.model.use_cases.general.GetUpcomingMoviesFromRepositoryUseCase
import com.daveloper.moviesapp.data.model.use_cases.room.GetUserFavoriteMoviesFromLocalDBUseCase
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val getPopularMoviesFromRepositoryUseCase: GetPopularMoviesFromRepositoryUseCase,
    private val getNowPlayingMoviesFromRepositoryUseCase: GetNowPlayingMoviesFromRepositoryUseCase,
    private val getUpcomingMoviesFromRepositoryUseCase: GetUpcomingMoviesFromRepositoryUseCase,
    private val getUserFavoriteMoviesFromLocalDBUseCase: GetUserFavoriteMoviesFromLocalDBUseCase
) {
    suspend fun getPopularMovies (
        internetConnection: Boolean,
        refresh: Boolean = false
    ) : List<Movie> {
        try {
            return getPopularMoviesFromRepositoryUseCase.getData(internetConnection, refresh)
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    suspend fun getUserFavoriteMovies (

    ): List<Movie> {
        try {
            return getUserFavoriteMoviesFromLocalDBUseCase.getData()
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    suspend fun getNowPlayingMovies (
        internetConnection: Boolean,
        refresh: Boolean = false
    ) : List<Movie> {
        try {
            return getNowPlayingMoviesFromRepositoryUseCase.getData(internetConnection, refresh)
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    suspend fun getUpcomingMovies (
        internetConnection: Boolean,
        refresh: Boolean = false
    ) : List<Movie> {
        try {
            return getUpcomingMoviesFromRepositoryUseCase.getData(internetConnection, refresh)
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}