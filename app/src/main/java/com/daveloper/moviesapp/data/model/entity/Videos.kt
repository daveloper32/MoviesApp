package com.daveloper.moviesapp.data.model.entity

import com.google.gson.annotations.SerializedName

data class Videos (
    @SerializedName("results") var videosFounded: List<Video>? = emptyList(), // List of Videos founded
)