package com.daveloper.moviesapp.data.model.entity

import com.google.gson.annotations.SerializedName

data class Actor (
    @SerializedName("name") var name: String? = "",
    @SerializedName("character") var characterName: String? = "",
    @SerializedName("profile_path") var profileImg: String? = "",
    var profileImgUrl: String? = "",
)