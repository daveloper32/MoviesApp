package com.daveloper.moviesapp.data.model.use_cases.general

import com.daveloper.moviesapp.core.MovieTypeProvider
import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.use_cases.api.GetSimilarMoviesFromAPIUseCase
import com.daveloper.moviesapp.data.model.use_cases.room.GetSimilarMoviesFromLocalDBUseCase
import timber.log.Timber
import javax.inject.Inject

class GetSimilarMoviesFromRepositoryUseCase @Inject constructor(
    private val api: GetSimilarMoviesFromAPIUseCase,
    private val dB: GetSimilarMoviesFromLocalDBUseCase,
    private val saveOrUpdateMoviesInLocalDBUseCase: SaveOrUpdateMoviesInLocalDBUseCase
) {
    suspend fun getData (
        movieId: Int,
        similarMoviesIds: List<Int>,
        internetConnection: Boolean,
        refresh: Boolean = false,
        languageCode: String = "en",
        countryCode: String = "US"
    ) : List<Movie> {
        try {
            return if (!refresh){
                // First we verify if the LocalDatabase is empty or not
                if (dB.getData(similarMoviesIds).isEmpty()) {
                    // If the device is connected we can call to the API
                    if (internetConnection) {
                        // get the response from the api
                        val data = api.getData(movieId, resultsPage = 2)
                        if (!data.isNullOrEmpty()) {
                            // save or update the data on local db
                            saveOrUpdateMoviesInLocalDBUseCase.saveOrUpdate(data,
                                0
                            )
                            // return the response from the api
                            dB.getData(similarMoviesIds)
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
                    dB.getData(similarMoviesIds)
                }
            } else {
                // If the device is connected we can call to the API
                if (internetConnection) {
                    // get the response from the api
                    val data = api.getData(movieId, resultsPage = 2)
                    if (!data.isNullOrEmpty()) {
                        // save or update the data on local db
                        saveOrUpdateMoviesInLocalDBUseCase.saveOrUpdate(data,
                            MovieTypeProvider.UPCOMING_MOVIE
                        )
                        // return the response from the api
                        dB.getData(similarMoviesIds)
                    } else {
                        Timber.e("GetUpcomingMoviesFromRepositoryUseCase couldn't found any value from  the API because ")
                        throw Exception("GetUpcomingMoviesFromRepositoryUseCase couldn't found any value from  the API because ")
                    }

                } else {
                    Timber.e("GetUpcomingMoviesFromRepositoryUseCase couldn't found any value from  the API because the internet connection is false, but it was resend the saved local db data")
                    dB.getData(similarMoviesIds)
                }
            }

        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}
