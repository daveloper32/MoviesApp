package com.daveloper.moviesapp.data.model.entity

import com.google.gson.annotations.SerializedName

data class MovieCast (
    @SerializedName("cast") var castFound: List<Actor>? = emptyList(), // List of Cast Actors found
)