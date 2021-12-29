package com.daveloper.moviesapp.data.model.use_cases.general

import com.daveloper.moviesapp.core.APIProvider
import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.network.movies_responses.MovieToSearchInfoService
import timber.log.Timber
import javax.inject.Inject

class SearchMovieFromRepositoryUseCase @Inject constructor(
    private val api: MovieToSearchInfoService,
    private val apiProvider: APIProvider
) {
    suspend fun getData (
        movieName: String,
        internetConnection: Boolean,
        refresh: Boolean = false
    ) : List<Movie> {
        try {
            return if (internetConnection) {
                // get the response from the api
                val data = api.searchMovies(movieName = movieName, resultsPage = 2)
                if (!data.isNullOrEmpty()) {
                    data.sortedByDescending {
                        it.releaseDate
                    }
                    data.forEach {
                        if (it.backdropPosterImg != null) {
                            it.backdropPosterImgURL = apiProvider.getImageMovieBaseUrl(it.backdropPosterImg!!)
                        }
                        if (it.posterImg != null) {
                            it.posterImgURL = apiProvider.getImageMovieBaseUrl(it.posterImg!!)
                        }
                    }
                    data
                } else {
                    Timber.e("SearchMovieFromRepositoryUseCase couldn't found any value from  the API because unknow error (maybe not found any movie)")
                    emptyList()
                }

            } else {
                Timber.e("SearchMovieFromRepositoryUseCase couldn't found any value from  the API because the internet connection is false")
                throw Exception("SearchMovieFromRepositoryUseCase couldn't found any value from  the API because the internet connection is false")
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}