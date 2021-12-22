package com.daveloper.moviesapp.data.network.movie_cast_responses

import com.daveloper.moviesapp.core.APIProvider
import com.daveloper.moviesapp.data.model.entity.Actor
import com.daveloper.moviesapp.data.model.entity.MovieCast
import com.daveloper.moviesapp.data.model.entity.Video
import com.daveloper.moviesapp.data.model.entity.Videos
import com.daveloper.moviesapp.data.network.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

class MovieCastInfoService  @Inject constructor(
    private val retrofit: Retrofit,
    private val apiProvider: APIProvider
) {
    suspend fun searchCast (
        movieId: Int,
        languageCode: String = "en",
        countryCode: String = "US"
    ): List<Actor>? {
        // Using other thread to call the API
        return withContext(Dispatchers.IO) {
            try {
                // Getting the retrofit response from the API converted to a MovieCast object
                val search = retrofit
                    .create(APIService::class.java)
                    .getMovieCast(
                        apiProvider.getMovieCreditsBaseURL(
                            movieId,
                            languageCode,
                            countryCode
                        )
                    )
                val movieCastInfo: MovieCast? = search?.body()
                // Return a list of Actor Objects
                Timber.i("¡Success! -> The Movie Cast were found")
                movieCastInfo?.castFound
            } catch (e: Exception) {
                Timber.e("Error trying to get the movie cast info from the API (Probably the movieId is incorrect or doesn't exist). Details -> Exception: $e")
                throw Exception(e)
            }
        }
    }
}