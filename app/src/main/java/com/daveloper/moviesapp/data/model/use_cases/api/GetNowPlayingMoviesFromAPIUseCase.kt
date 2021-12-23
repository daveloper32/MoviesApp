package com.daveloper.moviesapp.data.model.use_cases.api

import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.network.movies_responses.NowPlayingMoviesInfoService
import timber.log.Timber
import javax.inject.Inject

class GetNowPlayingMoviesFromAPIUseCase @Inject constructor(
    private val nowPlayingMoviesInfoService: NowPlayingMoviesInfoService
) {
    suspend fun getData (
        languageCode: String = "en",
        countryCode: String = "US",
        resultsPage: Int = 1
    ): List<Movie> {
        try {
            val data = nowPlayingMoviesInfoService.searchMovies(languageCode, countryCode, resultsPage)
            return if (!data.isNullOrEmpty()) {
                data
            } else {
                Timber.e("GetNowPlayingMoviesFromAPIUseCase couldn't found any value")
                emptyList()
            }

        } catch (e: Exception) {
            Timber.e(e)
            throw Exception(e)
        }
    }
}