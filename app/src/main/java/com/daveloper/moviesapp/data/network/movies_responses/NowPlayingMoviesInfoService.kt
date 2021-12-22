package com.daveloper.moviesapp.data.network.movies_responses

import com.daveloper.moviesapp.core.APIProvider
import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.entity.Movies
import com.daveloper.moviesapp.data.network.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

class NowPlayingMoviesInfoService @Inject constructor(
    private val retrofit: Retrofit,
    private val APIProvider: APIProvider
) {
    // Now Playing Movies
    suspend fun searchMovies (
        languageCode: String = "en",
        countryCode: String = "US",
        resultsPage: Int = 1
    ) : List<Movie> {
        // Using other thread to call the API
        return withContext(Dispatchers.IO) {
            try {
                // Getting the retrofit response from the API converted to a Movies object
                val movies: Movies? = getAPageOfMovies(languageCode, countryCode, resultsPage)
                // Verifying if the response is null
                if (movies != null) {
                    // Verifying if the attribute moviesFound (Array of Movie Object) is null
                    if (movies.moviesFound != null) {
                        // Verifying if the attribute pageMoviesFound (Int -> pages of movies that found the API) is null
                        if (movies.pageMoviesFound != null) {
                            // Creating and initializing a mutable list for save all the Movie objects found
                            val moviesInfoOfSomePages: MutableList<Movie> = ArrayList()
                            // Adding the Movie objects found on the first page
                            moviesInfoOfSomePages.addAll(movies.moviesFound!!)
                            /* Verifying if the pageMoviesFound attribute is >= to resultPage+1 in
                            * order to search for more movies in other page of the API
                            * */
                            if (movies.pageMoviesFound!! >= resultsPage+1) {
                                // We iterate from page 2 to resultPage
                                for (round in 2..resultsPage) {
                                    // Getting a new retrofit response from the API from the 'round' page
                                    val moviesPerRound = getAPageOfMovies(languageCode, countryCode, round)
                                    // Verifing if the response is null
                                    if (moviesPerRound != null) {
                                        // Verifying if the attribute moviesFound (Array of Movie Object) is null
                                        if (moviesPerRound.moviesFound != null) {
                                            // Adding the 'round' page of Movie object to the moviesInfoOfSomePages mutable list
                                            moviesInfoOfSomePages.addAll(moviesPerRound.moviesFound!!)
                                        }
                                    } else {
                                        // If the response is null we break the for
                                        break
                                    }
                                }
                                Timber.i("¡Success! -> It was found a total of ${moviesInfoOfSomePages.size} now playing movies on $resultsPage pages from the API")
                                // Return all the movies found on the 'resultsPage' pages
                                moviesInfoOfSomePages
                            } else {
                                Timber.i("¡Success/Wrong argument! -> It was found a total of ${moviesInfoOfSomePages.size} now playing movies but the result pages entered ($resultsPage) exceed the available pages from the API (${movies.pageMoviesFound}})")
                                // Return all the movies found only on the 1 page (the 'resultsPage' exceeds the number of pages that the API found)
                                moviesInfoOfSomePages
                            }
                        } else {
                            Timber.w("API don't get any value (no now playing movies pages)")
                            // Not movies found
                            movies.moviesFound ?: emptyList<Movie>()
                        }
                    } else {
                        Timber.w("API don't get any value (now playing movies)")
                        emptyList<Movie>()
                    }
                } else {
                    Timber.w("API don't get any value (now playing movies)")
                    emptyList<Movie>()
                }
            } catch (e: Exception) {
                Timber.e("Error trying to get the now playing movies from the API. Details -> Exception: $e")
                throw Exception(e)
            }
        }
    }

    private suspend fun getAPageOfMovies (
        languageCode: String,
        countryCode: String,
        resultsPage: Int
    ): Movies? {
        val search = retrofit
            .create(APIService::class.java)
            .getMovies(
                APIProvider.getNowPlayingMoviesBaseURL(
                    languageCode,
                    countryCode,
                    resultsPage
                )
            )
        return search?.body()
    }
}