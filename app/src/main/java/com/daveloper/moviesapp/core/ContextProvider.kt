package com.daveloper.moviesapp.core

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ContextProvider {
    @Singleton
    @Provides
    fun giveMeAppContext (
        @ApplicationContext context: Context
    ) : Context {
        return context
    }
}