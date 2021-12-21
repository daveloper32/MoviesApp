package com.daveloper.moviesapp.data.model.entity

import com.google.gson.annotations.SerializedName

data class Video (
    @SerializedName("name") var name: String? = "", // Name of the video
    @SerializedName("key") var keyFromVideo: String? = "", // https://www.youtube.com/watch?v= + key
    @SerializedName("site") var website: String? = "", // youtube or other
    @SerializedName("type") var type: String? = "", // Teaser, Featurette, Clip, Trailer
    @SerializedName("official") var isOfficial: Boolean? = false, // is a offical video?
)