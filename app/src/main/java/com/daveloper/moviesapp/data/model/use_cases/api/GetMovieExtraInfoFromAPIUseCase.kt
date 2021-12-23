package com.daveloper.moviesapp.data.model.use_cases.api

import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.network.movie_extra_details_responses.MovieExtraDetailsInfoService
import timber.log.Timber
import javax.inject.Inject

class GetMovieExtraInfoFromAPIUseCase @Inject constructor(
    private val movieExtraDetailsInfoService: MovieExtraDetailsInfoService
) {

    suspend fun getData (
        movieId: Int,
        languageCode: String = "en",
        countryCode: String = "US"
    ): Movie? {
        try {
            val data = movieExtraDetailsInfoService.searchMovie(movieId, languageCode, countryCode)
            return if (data != null) {
                data
            } else {
                Timber.w("GetMovieExtraInfoFromAPIUseCase couldn't get any value from the API")
                data
            }

        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}