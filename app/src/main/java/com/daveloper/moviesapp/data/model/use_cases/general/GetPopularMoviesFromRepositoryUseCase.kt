package com.daveloper.moviesapp.data.model.use_cases.general

import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.use_cases.api.GetPopularMoviesFromAPIUseCase
import com.daveloper.moviesapp.data.model.use_cases.room.GetPopularMoviesFromLocalDBUseCase
import timber.log.Timber
import javax.inject.Inject
import kotlin.Exception

class GetPopularMoviesFromRepositoryUseCase @Inject constructor(
    private val api: GetPopularMoviesFromAPIUseCase,
    private val dB: GetPopularMoviesFromLocalDBUseCase
) {
    suspend fun getData (
        internetConnection: Boolean
    ) : List<Movie> {
        try {
            // First we verify if the LocalDatabase is empty or not
            return if (dB.getData().isEmpty()) {
                // If the device is connected we can call to the API
                if (internetConnection) {
                    // return the response from the api
                    api.getData(resultsPage = 2)
                } else {
                    Timber.e("GetPopularMoviesFromRepositoryUseCase couldn't found any value from Local DB & its not possible to call the API because the internet connection is false")
                    throw Exception("GetPopularMoviesFromRepositoryUseCase couldn't found any value from Local DB & its not possible to call the API because the internet connection is false")
                }
            } else {
                // return the response from local DB
                dB.getData()
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}