package com.daveloper.moviesapp.core

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MovieTypeProvider {
    @Singleton
    const val POPULAR_MOVIE: Int = 1
    @Singleton
    const val NOW_PLAYING_MOVIE: Int = 2
    @Singleton
    const val UPCOMING_MOVIE: Int = 3
    @Singleton
    const val USER_FAVORITE_MOVIE: Int = 4
}