package com.daveloper.moviesapp.data.model.use_cases.api

import com.daveloper.moviesapp.data.model.entity.Review
import com.daveloper.moviesapp.data.network.movie_reviews_responses.MovieReviewsInfoService
import timber.log.Timber
import javax.inject.Inject

class GetMovieReviewsInfoFromAPIUseCase @Inject constructor(
    private val movieReviewsInfoService: MovieReviewsInfoService
) {
    suspend fun getData (
        movieId: Int,
        languageCode: String = "en",
        countryCode: String = "US"
    ): List<Review> {
        try {
            val data = movieReviewsInfoService.searchReviews(movieId, languageCode, countryCode)
            return if (data != null) {
                data
            } else {
                Timber.w("GetMovieReviewsInfoFromAPIUseCase couldn't get any value from the API")
                emptyList()
            }

        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}