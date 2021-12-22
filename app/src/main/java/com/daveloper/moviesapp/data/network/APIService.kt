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
    suspend fun getMovie (
        @Url url: String
    ): Response<Movie>?

    @GET
    suspend fun getVideos (
        @Url url: String
    ): Response<Video>?

    @GET
    suspend fun getMovieCast (
        @Url url: String
    ): Response<Actor>?

    @GET
    suspend fun getReviews (
        @Url url: String
    ): Response<Review>?
}