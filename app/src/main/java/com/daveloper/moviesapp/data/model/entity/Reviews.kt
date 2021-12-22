package com.daveloper.moviesapp.data.model.entity

import com.google.gson.annotations.SerializedName

data class Reviews (
    @SerializedName("results") var reviewsFound: List<Review>? = emptyList(), // List of Reviews found
)