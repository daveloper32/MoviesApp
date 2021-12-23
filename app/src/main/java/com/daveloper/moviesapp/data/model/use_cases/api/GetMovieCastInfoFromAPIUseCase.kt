package com.daveloper.moviesapp.data.model.use_cases.api

import com.daveloper.moviesapp.core.APIProvider
import com.daveloper.moviesapp.data.model.entity.Actor
import com.daveloper.moviesapp.data.network.movie_cast_responses.MovieCastInfoService
import timber.log.Timber
import javax.inject.Inject

class GetMovieCastInfoFromAPIUseCase @Inject constructor(
    private val movieCastInfoService: MovieCastInfoService,
    private val apiProvider: APIProvider
) {
    suspend fun getData (
        movieId: Int,
        languageCode: String = "en",
        countryCode: String = "US"
    ): List<Actor> {
        try {
            val data = movieCastInfoService.searchCast(movieId, languageCode, countryCode)
            return if (data != null) {
                // getting full url images
                 data.forEach { actor ->
                     actor.profileImgUrl = actor.profileImg?.let {
                         apiProvider.getImageMovieBaseUrl(
                             it
                         )
                     }
                 }
                data
            } else {
                Timber.w("GetMovieCastInfoFromAPIUseCase couldn't get any value from the API")
                emptyList()
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}