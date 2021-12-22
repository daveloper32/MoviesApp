package com.daveloper.moviesapp.core

import android.content.Context
import com.daveloper.moviesapp.auxiliar.ext_fun.activity_context.getStringResource
import javax.inject.Inject

class ResourceProvider @Inject constructor(
    private val giveMeAppContext: Context
) {
    fun getStringResource (
        strResource: Int
    ): String {
        val context = giveMeAppContext
        return context.getStringResource(strResource)
    }
}