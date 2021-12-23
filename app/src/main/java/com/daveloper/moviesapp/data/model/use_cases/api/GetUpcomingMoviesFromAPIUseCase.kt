package com.daveloper.moviesapp.data.model.use_cases.api

import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.network.movies_responses.UpcomingMoviesInfoService
import timber.log.Timber
import javax.inject.Inject

class GetUpcomingMoviesFromAPIUseCase @Inject constructor(
    private val upcomingMoviesInfoService: UpcomingMoviesInfoService
){
    suspend fun getData (
        languageCode: String = "en",
        countryCode: String = "US",
        resultsPage: Int = 1
    ): List<Movie> {
        try {
            val data = upcomingMoviesInfoService.searchMovies(languageCode, countryCode, resultsPage)
            return if (!data.isNullOrEmpty()) {
                data
            } else {
                Timber.e("GetUpcomingMoviesFromAPIUseCase couldn't found any value")
                emptyList()
            }

        } catch (e: Exception) {
            Timber.e(e)
            throw Exception(e)
        }
    }

}