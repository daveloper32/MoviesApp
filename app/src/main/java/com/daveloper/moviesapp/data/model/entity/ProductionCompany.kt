package com.daveloper.moviesapp.data.model.entity

import com.google.gson.annotations.SerializedName

data class ProductionCompany (
    @SerializedName("name") var name: String? = "",
    @SerializedName("logo_path") var logoImg: String? = "",
    @SerializedName("origin_country") var isoCountryCode: String? = "",
)