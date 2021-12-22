package com.daveloper.moviesapp.data.local_database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.daveloper.moviesapp.data.model.entity.Movie

@Dao
interface MovieDao {
    @Insert
    fun insertMovie(movie: Movie)

    @Query("SELECT * FROM movies WHERE id = :idMovie LIMIT 1")
    fun getMovie(idMovie: Int): Movie?

    @Query("SELECT * FROM movies WHERE isPopularMovie = 1")
    fun getPopularMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE isNowPlayingMovie = 1")
    fun getNowPlayingMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE isUpcomingMovie = 1")
    fun getUpcomingMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE isUserFavoriteMovie = 1")
    fun getUserFavoriteMovies(): List<Movie>

    @Update
    fun updateMovie(movie: Movie)
}