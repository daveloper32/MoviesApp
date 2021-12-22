package com.daveloper.moviesapp.data.local_database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.daveloper.moviesapp.data.local_database.room.converters.*
import com.daveloper.moviesapp.data.model.entity.Movie

// Creating the Database with room
// 1 Table (Entity) -> Movie (movies)
@Database (
    entities = [Movie::class],
    version = 1,
    exportSchema = false
)
// Adding converters to use lists of custom objects
@TypeConverters(
    value = [
        ActorConverter::class,
        GenreConverter::class,
        LanguageConverter::class,
        MovieConverter::class,
        ProductionCompanyConverter::class,
        ReviewConverter::class,
        VideoConverter::class
    ]
)
abstract class RoomMovieDatabase
    : RoomDatabase() {
        abstract fun movieDao(): MovieDao
}