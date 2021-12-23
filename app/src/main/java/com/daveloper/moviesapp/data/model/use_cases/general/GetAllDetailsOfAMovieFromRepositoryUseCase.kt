package com.daveloper.moviesapp.data.model.use_cases.general

import com.daveloper.moviesapp.core.APIProvider
import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.use_cases.api.*
import com.daveloper.moviesapp.data.model.use_cases.room.GetMovieFromLocalDBUseCase
import com.daveloper.moviesapp.data.model.use_cases.room.UpdateMovieInLocalDBUseCase
import javax.inject.Inject

class GetAllDetailsOfAMovieFromRepositoryUseCase @Inject constructor(
    private val getMovieFromLocalDBUseCase: GetMovieFromLocalDBUseCase,
    private val getMovieExtraInfoFromAPIUseCase: GetMovieExtraInfoFromAPIUseCase,
    private val updateMovieInLocalDBUseCase: UpdateMovieInLocalDBUseCase,
    private val getRelatedVideosOfAMovieFromAPIUseCase: GetRelatedVideosOfAMovieFromAPIUseCase,
    private val getMovieCastInfoFromAPIUseCase: GetMovieCastInfoFromAPIUseCase,
    private val getMovieReviewsInfoFromAPIUseCase: GetMovieReviewsInfoFromAPIUseCase,
    private val getSimilarMoviesFromAPIUseCase: GetSimilarMoviesFromAPIUseCase,
    private val apiProvider: APIProvider
) {
    suspend fun getData (
        movieId: Int,
        internetConnection: Boolean,
        refresh: Boolean = false,
        languageCode: String = "en",
        countryCode: String = "US"
    ): Movie {
        try {
            if (refresh) {
                val savedLocalData = getMovieFromLocalDBUseCase.getData(movieId)
                return if (savedLocalData != null) {
                    onlyGetFromInternetWithSavedData(savedLocalData, internetConnection, languageCode, countryCode)
                } else {
                    onlyGetFromInternetByMovieId(movieId, internetConnection, languageCode, countryCode)
                }
            } else {
                val savedLocalData = getMovieFromLocalDBUseCase.getData(movieId)
                if (savedLocalData != null) {
                    if (savedLocalData.genres != null ||
                        savedLocalData.webPage != null ||
                        savedLocalData.tagline != null ||
                        savedLocalData.spokenLanguages != null ||
                        savedLocalData.productionCompanies != null
                    ) {
                        val allData = getExtraData(savedLocalData, internetConnection, languageCode, countryCode)
                        // Verify if allData is null
                        if (allData != null) {
                            // Update in local db
                            updateMovieInLocalDBUseCase.updateData(allData)
                            return allData
                        } else {
                            throw Exception("Null exception -> No Movie data found")
                        }

                    } else {
                        return onlyGetFromInternetWithSavedData(savedLocalData, internetConnection, languageCode, countryCode)
                    }

                } else {
                    return onlyGetFromInternetByMovieId(movieId, internetConnection, languageCode, countryCode)
                }
            }
        } catch (e:Exception) {
            throw Exception(e)
        }
    }

    private suspend fun onlyGetFromInternetWithSavedData(
        savedLocalData: Movie,
        internetConnection: Boolean,
        languageCode: String,
        countryCode: String
    ): Movie {
        try {
            // Only info from API
            if (internetConnection) {
                // Get extra information of the Movie
                val data = getMovieExtraInfoFromAPIUseCase.getData(savedLocalData.id, languageCode, countryCode)
                if (data != null) {
                    savedLocalData.genres = data.genres
                    savedLocalData.webPage = data.webPage
                    savedLocalData.tagline = data.tagline
                    savedLocalData.spokenLanguages = data.spokenLanguages
                    savedLocalData.productionCompanies = data.productionCompanies
                }
                // Getting and adding movie videos, cast, reviews & similar movies to the current data
                val allData = getExtraData(savedLocalData, internetConnection, languageCode, countryCode)
                // Verify if allData is null
                if (allData != null) {
                    // Update in local db
                    updateMovieInLocalDBUseCase.updateData(allData)
                    return allData
                } else {
                    throw Exception("Null exception -> No Movie data found")
                }
            } else {
                throw Exception("No movie data found, not internet connection available")
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    private suspend fun onlyGetFromInternetByMovieId(
        movieId: Int,
        internetConnection: Boolean,
        languageCode: String = "en",
        countryCode: String = "US"
    ): Movie {
        try {
            // Only info from API
            if (internetConnection) {
                // Get extra information of the Movie
                val data = getMovieExtraInfoFromAPIUseCase.getData(movieId, languageCode, countryCode)
                // Getting and adding movie videos, cast, reviews & similar movies to the current data
                val allData = data?.let { getExtraData(it, internetConnection, languageCode, countryCode) }
                // Verify if allData is null
                if (allData != null) {
                    // Update in local db
                    updateMovieInLocalDBUseCase.updateData(allData)
                    return allData
                } else {
                    throw Exception("Null exception -> No Movie data found")
                }
            } else {
                throw Exception("No movie data found, not internet connection available")
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
    }

    private suspend fun getExtraData(
        movieData: Movie,
        internetConnection: Boolean,
        languageCode: String = "en",
        countryCode: String = "US"
    ): Movie {
        try {
            if(internetConnection){
                // Movie videos
                val videos = getRelatedVideosOfAMovieFromAPIUseCase.getData(movieData.id, languageCode, countryCode)
                // Movie cast
                val cast = getMovieCastInfoFromAPIUseCase.getData(movieData.id, languageCode, countryCode)
                // Movie reviews
                val reviews = getMovieReviewsInfoFromAPIUseCase.getData(movieData.id, languageCode, countryCode)
                // Movie Similar movies
                val similarMovies = getSimilarMoviesFromAPIUseCase.getData(movieData.id, languageCode, countryCode, 2)
                // Add all new data to current movieData
                movieData.videos = videos
                movieData.cast = cast
                movieData.reviews = reviews
                movieData.similarMovies = similarMovies
                // update on local db
                updateMovieInLocalDBUseCase.updateData(movieData)
                return movieData
            } else {
                throw Exception("No internet connection, couldn't get videos, cast, reviews & similar movies")
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}