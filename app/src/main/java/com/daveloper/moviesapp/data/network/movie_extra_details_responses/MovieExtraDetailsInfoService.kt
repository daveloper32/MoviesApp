package com.daveloper.moviesapp.data.network.movie_extra_details_responses

import com.daveloper.moviesapp.core.APIProvider
import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.network.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

class MovieExtraDetailsInfoService @Inject constructor(
    private val retrofit: Retrofit,
    private val apiProvider: APIProvider
) {
    suspend fun searchMovie (
        movieId: Int,
        languageCode: String = "en",
        countryCode: String = "US"
    ): Movie? {
        // Using other thread to call the API
        return withContext(Dispatchers.IO) {
            try {
                // Getting the retrofit response from the API converted to a Movie object
                val search = retrofit
                    .create(APIService::class.java)
                    .getMovieExtraDetails(
                        apiProvider.getMovieDetailsInfoBaseURL(
                            movieId,
                            languageCode,
                            countryCode
                        )
                    )
                val movieExtraInfo = search?.body()
                // Return the extra info of the Movie
                Timber.i("¡Success! -> The Movie was found")
                movieExtraInfo
            } catch (e: Exception) {
                Timber.e("Error trying to get the movie extra info from the API (Probably the movieId is incorrect or doesn't exist). Details -> Exception: $e")
                throw Exception(e)
            }
        }
    }
}