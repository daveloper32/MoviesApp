package com.daveloper.moviesapp.data.model.use_cases.general

import com.daveloper.moviesapp.core.MovieTypeProvider.POPULAR_MOVIE
import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.use_cases.api.GetPopularMoviesFromAPIUseCase
import com.daveloper.moviesapp.data.model.use_cases.room.GetPopularMoviesFromLocalDBUseCase
import timber.log.Timber
import javax.inject.Inject
import kotlin.Exception

class GetPopularMoviesFromRepositoryUseCase @Inject constructor(
    private val api: GetPopularMoviesFromAPIUseCase,
    private val dB: GetPopularMoviesFromLocalDBUseCase,
    private val saveOrUpdateMoviesInLocalDBUseCase: SaveOrUpdateMoviesInLocalDBUseCase
) {
    suspend fun getData (
        internetConnection: Boolean
    ) : List<Movie> {
        try {
            // First we verify if the LocalDatabase is empty or not
            return if (dB.getData().isEmpty()) {
                // If the device is connected we can call to the API
                if (internetConnection) {
                    // get the response from the api
                    val data = api.getData(resultsPage = 2)
                    if (!data.isNullOrEmpty()) {
                        // save or update the data on local db
                        saveOrUpdateMoviesInLocalDBUseCase.saveOrUpdate(data, POPULAR_MOVIE)
                        // return the response from the api
                        data
                    } else {
                        Timber.e("GetPopularMoviesFromRepositoryUseCase couldn't found any value from  the API because ")
                        throw Exception("GetPopularMoviesFromRepositoryUseCase couldn't found any value from  the API because ")
                    }

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