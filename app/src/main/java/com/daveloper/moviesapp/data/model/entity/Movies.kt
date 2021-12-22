package com.daveloper.moviesapp.data.model.entity

import com.google.gson.annotations.SerializedName

data class Movies (
    @SerializedName("page") var pageMoviesFound: Int? = -1, // Page of 19 results of Movies founded
    @SerializedName("results") var moviesFound: List<Movie>? = emptyList(), // List of Movies founded
    @SerializedName("total_pages") var totalPagesMoviesFound: Int? = -1, // Total Pages of results of Movies founded
    @SerializedName("total_results") var totalResultsMoviesFound: Int? = -1, // Total Results of Movies founded
)