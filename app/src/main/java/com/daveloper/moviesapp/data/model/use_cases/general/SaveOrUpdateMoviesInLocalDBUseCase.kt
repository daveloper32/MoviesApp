package com.daveloper.moviesapp.data.model.use_cases.general

import com.daveloper.moviesapp.core.APIProvider
import com.daveloper.moviesapp.core.MovieTypeProvider.NOW_PLAYING_MOVIE
import com.daveloper.moviesapp.core.MovieTypeProvider.POPULAR_MOVIE
import com.daveloper.moviesapp.core.MovieTypeProvider.UPCOMING_MOVIE
import com.daveloper.moviesapp.core.MovieTypeProvider.USER_FAVORITE_MOVIE
import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.use_cases.room.GetMovieFromLocalDBUseCase
import com.daveloper.moviesapp.data.model.use_cases.room.InsertMovieInLocalDBUseCase
import com.daveloper.moviesapp.data.model.use_cases.room.UpdateMovieInLocalDBUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SaveOrUpdateMoviesInLocalDBUseCase @Inject constructor(
    private val getMovie: GetMovieFromLocalDBUseCase,
    private val insertMovie: InsertMovieInLocalDBUseCase,
    private val updateMovie: UpdateMovieInLocalDBUseCase
){
    suspend fun saveOrUpdate (
        movies: List<Movie>,
        movieType: Int
    ) {
        withContext(Dispatchers.IO) {
            try {
                if (!movies.isNullOrEmpty()) {
                    for (movie in movies) {
                        var dataExist = getMovie.getData(movie.id)
                        // Verify if the Movie exist on the Local Database
                        if (dataExist != null) {
                            val movieToUpdate = basicCopyOfMovie(dataExist, movieType)
                            // If exist we need to update the existing Movie with a extra movieType value
                            updateMovie.updateData(movieToUpdate)
                        } else {
                            val movieToInsert = basicCopyOfMovie(movie, movieType)
                            // if doesnt exist we need to insert
                            insertMovie.insertData(movieToInsert)
                        }
                    }
                } else {
                    Timber.e("SaveOrUpdateMoviesinLocalDBUseCase couldn't save or update anything beacause the entry list of Movie objects is empty or null")
                    throw Exception("SaveOrUpdateMoviesinLocalDBUseCase couldn't save or update anything beacause the entry list of Movie objects is empty or null")
                }
            } catch (e: Exception) {
                throw Exception(e)
            }
        }
    }

    private fun basicCopyOfMovie (
        movie: Movie,
        movieType: Int
    ): Movie {
        // We need to add the Movie type
        // and get the URL image for poster and backdrop poster
        return when (movieType) {
            POPULAR_MOVIE -> {
                movie.isPopularMovie = true
                movie.posterImgURL = APIProvider().getImageMovieBaseUrl(movie.posterImg?: "")
                movie.backdropPosterImgURL = APIProvider().getImageMovieBaseUrl(movie.backdropPosterImg?: "")
                movie
            }
            NOW_PLAYING_MOVIE -> {
                movie.isNowPlayingMovie = true
                movie.posterImgURL = APIProvider().getImageMovieBaseUrl(movie.posterImg?: "")
                movie.backdropPosterImgURL = APIProvider().getImageMovieBaseUrl(movie.backdropPosterImg?: "")
                movie
            }
            UPCOMING_MOVIE -> {
                movie.isUpcomingMovie = true
                movie.posterImgURL = APIProvider().getImageMovieBaseUrl(movie.posterImg?: "")
                movie.backdropPosterImgURL = APIProvider().getImageMovieBaseUrl(movie.backdropPosterImg?: "")
                movie
            }
            USER_FAVORITE_MOVIE -> {
                movie.isUserFavoriteMovie = true
                movie.posterImgURL = APIProvider().getImageMovieBaseUrl(movie.posterImg?: "")
                movie.backdropPosterImgURL = APIProvider().getImageMovieBaseUrl(movie.backdropPosterImg?: "")
                movie
            }
            else -> {
                movie.posterImgURL = APIProvider().getImageMovieBaseUrl(movie.posterImg?: "")
                movie.backdropPosterImgURL = APIProvider().getImageMovieBaseUrl(movie.backdropPosterImg?: "")
                movie
            }
        }
    }
}