package com.daveloper.moviesapp.data.network

import com.daveloper.moviesapp.data.model.entity.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getMovies (
        @Url url: String
    ): Response<Movies>?

    @GET
    suspend fun getMovieExtraDetails (
        @Url url: String
    ): Response<Movie>?

    @GET
    suspend fun getMovieVideos (
        @Url url: String
    ): Response<Videos>?

    @GET
    suspend fun getMovieCast (
        @Url url: String
    ): Response<MovieCast>?

    @GET
    suspend fun getMovieReviews (
        @Url url: String
    ): Response<Reviews>?
}