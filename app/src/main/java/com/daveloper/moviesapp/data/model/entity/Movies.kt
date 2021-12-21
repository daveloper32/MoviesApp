package com.daveloper.moviesapp.data.model.entity

import com.google.gson.annotations.SerializedName

data class Movies (
    @SerializedName("page") var pageMoviesFounded: Int? = -1, // Page of 19 results of Movies founded
    @SerializedName("results") var moviesFounded: List<Movie>? = emptyList(), // List of Movies founded
    @SerializedName("total_pages") var totalPagesMoviesFounded: Int? = -1, // Total Pages of results of Movies founded
    @SerializedName("total_results") var totalResultsMoviesFounded: Int? = -1, // Total Results of Movies founded
)