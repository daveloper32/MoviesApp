package com.daveloper.moviesapp.core

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitProvider {
    private const val apiTheSportsDB: String =
        "https://api.themoviedb.org/3/"

    /* Build of retrofit with the base URL of the API using Dagger to only make one instance of Retrofit
        that could be could from anywhere
    */
    @Singleton
    @Provides
    fun getRetrofit() : Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(apiTheSportsDB)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}