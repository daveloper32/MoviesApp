package com.daveloper.moviesapp.data.model.use_cases.api

import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.network.movies_responses.SimilarMoviesInfoService
import timber.log.Timber
import javax.inject.Inject

class GetSimilarMoviesFromAPIUseCase @Inject constructor(
    private val similarMoviesInfoService: SimilarMoviesInfoService
) {
    suspend fun getData (
        movieId: Int,
        languageCode: String = "en",
        countryCode: String = "US",
        resultsPage: Int = 1
    ): List<Movie> {
        try {
            val data = similarMoviesInfoService.searchMovies(movieId, languageCode, countryCode, resultsPage)
            return if (!data.isNullOrEmpty()) {
                data
            } else {
                Timber.e("GetSimilarMoviesFromAPIUseCase couldn't found any value")
                //throw Exception("GetPopularMoviesFromAPIUseCase couldn't found any value")
                emptyList()
            }

        } catch (e: Exception) {
            Timber.e(e)
            throw Exception(e)
        }
    }
}