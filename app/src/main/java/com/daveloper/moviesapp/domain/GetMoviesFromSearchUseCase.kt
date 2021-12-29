package com.daveloper.moviesapp.domain

import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.repository.SearchMovieRepository
import javax.inject.Inject

class GetMoviesFromSearchUseCase @Inject constructor(
    private val repository: SearchMovieRepository
) {

    suspend fun getData (
        movieName: String,
        internetConnection: Boolean,
        refresh: Boolean = false
    ) : List<Movie> {
        try {
            return repository.getMoviesFromSearch(movieName, internetConnection, refresh)
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}