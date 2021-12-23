package com.daveloper.moviesapp.data.model.use_cases.general

import com.daveloper.moviesapp.core.MovieTypeProvider
import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.use_cases.api.GetUpcomingMoviesFromAPIUseCase
import com.daveloper.moviesapp.data.model.use_cases.room.GetUpcomingMoviesFromLocalDBUseCase
import timber.log.Timber
import javax.inject.Inject

class GetUpcomingMoviesFromRepositoryUseCase @Inject constructor(
    private val api: GetUpcomingMoviesFromAPIUseCase,
    private val dB: GetUpcomingMoviesFromLocalDBUseCase,
    private val saveOrUpdateMoviesInLocalDBUseCase: SaveOrUpdateMoviesInLocalDBUseCase
){
    suspend fun getData (
        internetConnection: Boolean,
        refresh: Boolean = false
    ) : List<Movie> {
        try {
            return if (!refresh){
                // First we verify if the LocalDatabase is empty or not
                if (dB.getData().isEmpty()) {
                    // If the device is connected we can call to the API
                    if (internetConnection) {
                        // get the response from the api
                        val data = api.getData(resultsPage = 2)
                        if (!data.isNullOrEmpty()) {
                            // save or update the data on local db
                            saveOrUpdateMoviesInLocalDBUseCase.saveOrUpdate(data,
                                MovieTypeProvider.UPCOMING_MOVIE
                            )
                            // return the response from the api
                            dB.getData()
                        } else {
                            Timber.e("GetUpcomingMoviesFromRepositoryUseCase couldn't found any value from  the API because ")
                            throw Exception("GetUpcomingMoviesFromRepositoryUseCase couldn't found any value from  the API because ")
                        }

                    } else {
                        Timber.e("GetUpcomingMoviesFromRepositoryUseCase couldn't found any value from Local DB & its not possible to call the API because the internet connection is false")
                        throw Exception("GetUpcomingMoviesFromRepositoryUseCase couldn't found any value from Local DB & its not possible to call the API because the internet connection is false")
                    }
                } else {
                    // return the response from local DB
                    dB.getData()
                }
            } else {
                // If the device is connected we can call to the API
                if (internetConnection) {
                    // get the response from the api
                    val data = api.getData(resultsPage = 2)
                    if (!data.isNullOrEmpty()) {
                        // save or update the data on local db
                        saveOrUpdateMoviesInLocalDBUseCase.saveOrUpdate(data,
                            MovieTypeProvider.UPCOMING_MOVIE
                        )
                        // return the response from the api
                        dB.getData()
                    } else {
                        Timber.e("GetUpcomingMoviesFromRepositoryUseCase couldn't found any value from  the API because ")
                        throw Exception("GetUpcomingMoviesFromRepositoryUseCase couldn't found any value from  the API because ")
                    }

                } else {
                    Timber.e("GetUpcomingMoviesFromRepositoryUseCase couldn't found any value from Local DB & its not possible to call the API because the internet connection is false")
                    throw Exception("GetUpcomingMoviesFromRepositoryUseCase couldn't found any value from Local DB & its not possible to call the API because the internet connection is false")
                }
            }

        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}