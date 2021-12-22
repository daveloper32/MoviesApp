package com.daveloper.moviesapp.domain

import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.repository.MoviesRepository
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository
) {
    suspend fun getData (
        internetConnection: Boolean
    ) : List<Movie> {
        try {
            return moviesRepository.getPopularMovies(internetConnection)
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}