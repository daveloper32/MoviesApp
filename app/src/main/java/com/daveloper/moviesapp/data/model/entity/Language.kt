package com.daveloper.moviesapp.data.model.entity

import com.google.gson.annotations.SerializedName

data class Language (
    @SerializedName("name") var name: String? = "",
    @SerializedName("english_name") var englishName: String? = "",
    @SerializedName("iso_639_1") var isoLanguageCode: String? = "",
)