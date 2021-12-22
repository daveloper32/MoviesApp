package com.daveloper.moviesapp.data.network.movie_reviews_responses

import com.daveloper.moviesapp.core.APIProvider
import com.daveloper.moviesapp.data.model.entity.Review
import com.daveloper.moviesapp.data.model.entity.Reviews
import com.daveloper.moviesapp.data.network.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

class MovieReviewsInfoService @Inject constructor(
    private val retrofit: Retrofit,
    private val apiProvider: APIProvider
) {
    suspend fun searchReviews (
        movieId: Int,
        languageCode: String = "en",
        countryCode: String = "US"
    ): List<Review>? {
        // Using other thread to call the API
        return withContext(Dispatchers.IO) {
            try {
                // Getting the retrofit response from the API converted to a MovieCast object
                val search = retrofit
                    .create(APIService::class.java)
                    .getMovieReviews(
                        apiProvider.getMovieReviewsBaseURL(
                            movieId,
                            languageCode,
                            countryCode
                        )
                    )
                val movieReviewsInfo: Reviews? = search?.body()
                // Return a list of Actor Objects
                Timber.i("¡Success! -> The Movie Reviews were found")
                movieReviewsInfo?.reviewsFound
            } catch (e: Exception) {
                Timber.e("Error trying to get the movie reviews info from the API (Probably the movieId is incorrect or doesn't exist). Details -> Exception: $e")
                throw Exception(e)
            }
        }
    }
}