package com.daveloper.moviesapp.data.model.use_cases.api

import com.daveloper.moviesapp.data.model.entity.Video
import com.daveloper.moviesapp.data.network.movie_videos_responses.MovieVideosInfoService
import timber.log.Timber
import javax.inject.Inject

class GetRelatedVideosOfAMovieFromAPIUseCase @Inject constructor(
    private val movieVideosInfoService: MovieVideosInfoService
) {
    suspend fun getData (
        movieId: Int,
        languageCode: String = "en",
        countryCode: String = "US"
    ): List<Video> {
        try {
            val data = movieVideosInfoService.searchVideos(movieId, languageCode, countryCode)
            return if (data != null){
                data
            } else {
                Timber.w("GetRelatedVideosOfAMovieFromAPIUseCase couldn't get any value from the API")
                emptyList()
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}