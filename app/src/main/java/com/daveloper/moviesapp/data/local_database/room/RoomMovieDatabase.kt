package com.daveloper.moviesapp.data.local_database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.daveloper.moviesapp.data.model.entity.Movie

// Creating the Database with room
// 1 Table (Entity) -> Movie (movies)
@Database (
    entities = [Movie::class],
    version = 1,
    exportSchema = false
)
abstract class RoomMovieDatabase
    : RoomDatabase() {
        abstract fun movieDao(): MovieDao
}