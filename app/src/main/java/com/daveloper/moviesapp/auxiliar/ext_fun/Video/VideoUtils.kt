package com.daveloper.moviesapp.auxiliar.ext_fun.Video

import com.daveloper.moviesapp.data.model.entity.Video

private const val baseUrlVideo: String = "https://www.youtube.com/watch?v="

fun Video.getBaseUrlVideo (): String {
    return if (!this.keyFromVideo.isNullOrEmpty()) {
        baseUrlVideo + this.keyFromVideo
    } else {
        ""
    }
}