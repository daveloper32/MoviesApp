package com.daveloper.moviesapp.data.model.entity

import com.google.gson.annotations.SerializedName

data class ReviewAuthor (
    @SerializedName("name") var name: String? = "",
    @SerializedName("username") var userName: String? = "",
    @SerializedName("avatar_path") var userImg: String? = "",
    @SerializedName("rating") var rating: Float? = -1.0f,
)