package com.daveloper.moviesapp.data.model.use_cases.api

import com.daveloper.moviesapp.core.APIProvider
import timber.log.Timber
import javax.inject.Inject

class GetImgMovieUrlUseCase @Inject constructor(
    private val apiProvider: APIProvider
) {
    fun get (
        imgPath: String,
        fileSize: Int // 200, 300, 400, 500
    ) : String {
        try {
            if (imgPath.isNotEmpty()) {
                return apiProvider.getImageMovieBaseUrl(
                    imgPath,
                    "w$fileSize"
                )
            } else {
                Timber.e("Error getting Img Movie URL -> Empty or null imgPath")
                throw Exception("Error getting Img Movie URL -> Empty or null imgPath")
            }
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
}