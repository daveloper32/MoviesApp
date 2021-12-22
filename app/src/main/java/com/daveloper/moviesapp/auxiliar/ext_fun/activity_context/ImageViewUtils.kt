package com.daveloper.moviesapp.auxiliar.ext_fun.activity_context

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage (
    urlImage: String,
    circleCrop: Boolean = true,
    errorImage: Int
) {
    if (urlImage.isNotEmpty()) {
        // Si circleCrop es true cargamos la imagen de forma circular
        if (circleCrop) {
            Glide
                .with(this.context)
                .load(urlImage)
                .circleCrop()
                .error(errorImage)
                .into(this)
            // Si circleCrop es false cargamos la imagen de forma normal
        } else {
            Glide
                .with(this.context)
                .load(urlImage)
                .error(errorImage)
                .into(this)
        }
    }
}