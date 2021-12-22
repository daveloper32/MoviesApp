package com.daveloper.moviesapp.core

import android.content.Context
import androidx.room.Room
import com.daveloper.moviesapp.data.local_database.room.MovieDao
import com.daveloper.moviesapp.data.local_database.room.RoomMovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomProvider {

    // Singleton function that returns a unique Instance of the Database
    @Singleton
    @Provides
    fun moviesDatabase (
        @ApplicationContext context: Context
    ): RoomMovieDatabase {
        return Room
            .databaseBuilder(
                context,
                RoomMovieDatabase::class.java,
                "local_database"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    // Singleton function that returns a unique Instance of the Dao of the Movie Database
    @Singleton
    @Provides
    fun movieDao (
        movieDatabase: RoomMovieDatabase
    ): MovieDao {
        return movieDatabase.movieDao()
    }
}