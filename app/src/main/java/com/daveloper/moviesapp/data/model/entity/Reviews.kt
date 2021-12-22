package com.daveloper.moviesapp.data.model.entity

import com.google.gson.annotations.SerializedName

data class Reviews (
    @SerializedName("results") var reviewsFounded: List<Review>? = emptyList(), // List of Reviews founded
)