package com.daveloper.moviesapp.data.model.entity

import com.google.gson.annotations.SerializedName

data class Review (
    @SerializedName("author") var author: String? = "",
    @SerializedName("author_details") var authorReview: ReviewAuthor,
    @SerializedName("content") var content: String? = "",
    @SerializedName("updated_at") var date: String? = "",
)