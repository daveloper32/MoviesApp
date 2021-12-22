package com.daveloper.moviesapp.data.network.movie_videos_responses

import com.daveloper.moviesapp.core.APIProvider
import com.daveloper.moviesapp.data.model.entity.Video
import com.daveloper.moviesapp.data.model.entity.Videos
import com.daveloper.moviesapp.data.network.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

class MovieVideosInfoService @Inject constructor(
    private val retrofit: Retrofit,
    private val apiProvider: APIProvider
) {
    suspend fun searchMovie (
        movieId: Int,
        languageCode: String = "en",
        countryCode: String = "US"
    ): List<Video>? {
        // Using other thread to call the API
        return withContext(Dispatchers.IO) {
            try {
                // Getting the retrofit response from the API converted to a Videos object
                val search = retrofit
                    .create(APIService::class.java)
                    .getMovieVideos(
                        apiProvider.getMovieVideosBaseURL(
                            movieId,
                            languageCode,
                            countryCode
                        )
                    )
                val movieVideosInfo: Videos? = search?.body()
                // Return a list of Video Objects
                Timber.i("¡Success! -> The Movie Videos were found")
                movieVideosInfo?.videosFound
            } catch (e: Exception) {
                Timber.e("Error trying to get the movie videos info from the API (Probably the movieId is incorrect or doesn't exist). Details -> Exception: $e")
                throw Exception(e)
            }
        }
    }
}