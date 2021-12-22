package com.daveloper.moviesapp.core

import javax.inject.Inject


class APIProvider @Inject constructor(

) {
    // My https://www.themoviedb.org API key
    private val apiKey = "16be3951af16a8f7f72f38f2a1679ec4"
    // Base URL
    private val baseURL = "movie/"
    private val baseURLForSearch = "search/movie/"
    // Base Image Resource URL
    private val baseImgResURL = "https://image.tmdb.org/t/p/"
    // Getting Popular Movies URL
    // 19 result per page
    // https://developers.themoviedb.org/3/movies/get-popular-movies
    fun getPopularMoviesBaseURL (
        languageCode: String = "en", // ISO 639-1 language code value
        countryCode: String = "US", // ISO 3166-1 country code value (only Uppercase accepted)
        resultsPage: Int = 1 // minimum val = 1 - maximum val = 1000
    ): String {
        return "${baseURL}popular?api_key=${apiKey}&language=${languageCode}-${countryCode}&page=${resultsPage}"
    }
    // Getting Now Playing Movies URL
    // https://developers.themoviedb.org/3/movies/get-now-playing
    fun getNowPlayingMoviesBaseURL (
        languageCode: String = "en",
        countryCode: String = "US",
        resultsPage: Int = 1
    ): String {
        return "${baseURL}now_playing?api_key=${apiKey}&language=${languageCode}-${countryCode}&page=${resultsPage}"
    }
    // Getting Upcoming Movies URL
    // https://developers.themoviedb.org/3/movies/get-upcoming
    fun getUpcomingMoviesBaseURL (
        languageCode: String = "en",
        countryCode: String = "US",
        resultsPage: Int = 1
    ): String {
        return "${baseURL}upcoming?api_key=${apiKey}&language=${languageCode}-${countryCode}&page=${resultsPage}"
    }
    // Getting Full URL image resource from a movie
    // https://developers.themoviedb.org/3/getting-started/images
    fun getImageMovieBaseUrl (
        imagePath: String, //basically name of the image . (jpg, png, etc)
        fileSize: String = "original" // I think only works with w(200, 300, 400 $ 500) or original
    ): String {
        return "${baseImgResURL}${fileSize}/${imagePath}"
    }
    // Getting Details Information about a movie
    // https://developers.themoviedb.org/3/movies/get-movie-details
    fun getMovieDetailsInfoBaseURL (
        movieID: Int, // int value assigned by TheMovieDB
        languageCode: String = "en",
        countryCode: String = "US"
    ): String {
        return "${baseURL}${movieID}?api_key=${apiKey}&language=${languageCode}-${countryCode}"
    }
    // Getting Videos related to the movie (trailers, bloopers, promotions, ...)
    // https://developers.themoviedb.org/3/movies/get-movie-videos
    fun getMovieVideosBaseURL (
        movieID: Int,
        languageCode: String = "en",
        countryCode: String = "US"
    ): String {
        return "${baseURL}${movieID}/videos?api_key=${apiKey}&language=${languageCode}-${countryCode}"
    }
    // Getting Credits of a movie
    // https://developers.themoviedb.org/3/movies/get-movie-credits
    fun getMovieCreditsBaseURL (
        movieID: Int,
        languageCode: String = "en",
        countryCode: String = "US"
    ): String {
        return "${baseURL}${movieID}/credits?api_key=${apiKey}&language=${languageCode}-${countryCode}"
    }
    // Getting Reviews of a movie
    // https://developers.themoviedb.org/3/movies/get-movie-reviews
    fun getMovieReviewsBaseURL (
        movieID: Int,
        languageCode: String = "en",
        countryCode: String = "US",
        resultsPage: Int = 1
    ): String {
        return "${baseURL}${movieID}/reviews?api_key=${apiKey}&language=${languageCode}-${countryCode}&page=${resultsPage}"
    }
    // Getting Similar or Related Movies
    // https://developers.themoviedb.org/3/movies/get-similar-movies
    fun getMovieSimilar (
        movieID: Int,
        languageCode: String = "en",
        countryCode: String = "US",
        resultsPage: Int = 1
    ): String {
        return "${baseURL}${movieID}/similar?api_key=${apiKey}&language=${languageCode}-${countryCode}&page=${resultsPage}"
    }
    // Searching a movie
    // https://developers.themoviedb.org/3/search/search-movies
    fun getMovieToSearch (
        movieName: String, // Movie name typed by the user
        languageCode: String = "en",
        countryCode: String = "US",
        resultsPage: Int = 1
    ): String {
        return "${baseURLForSearch}?api_key=${apiKey}&language=${languageCode}-${countryCode}&query=${movieName}&page=${resultsPage}"
    }
}