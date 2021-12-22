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
                        // Verify if the Movie exist on the Local Database
                        if (getMovie.getData(movie.id) != null) {
                            val movieToUpdate = basicCopyOfMovie(movie, movieType)
                            // If exist we need to update
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
            POPULAR_MOVIE -> movie
                .copy(
                    isPopularMovie = true,
                    posterImgURL = APIProvider().getImageMovieBaseUrl(movie.posterImg?: ""),
                    backdropPosterImgURL = APIProvider().getImageMovieBaseUrl(movie.backdropPosterImgURL?: "")
                )
            NOW_PLAYING_MOVIE -> movie
                .copy(
                    isNowPlayingMovie = true,
                    posterImgURL = APIProvider().getImageMovieBaseUrl(movie.posterImg?: ""),
                    backdropPosterImgURL = APIProvider().getImageMovieBaseUrl(movie.backdropPosterImgURL?: "")
                )
            UPCOMING_MOVIE -> movie
                .copy(
                    isUpcomingMovie = true,
                    posterImgURL = APIProvider().getImageMovieBaseUrl(movie.posterImg?: ""),
                    backdropPosterImgURL = APIProvider().getImageMovieBaseUrl(movie.backdropPosterImgURL?: "")
                )
            USER_FAVORITE_MOVIE -> movie
                .copy(
                    isUserFavoriteMovie = true,
                    posterImgURL = APIProvider().getImageMovieBaseUrl(movie.posterImg?: ""),
                    backdropPosterImgURL = APIProvider().getImageMovieBaseUrl(movie.backdropPosterImgURL?: "")
                )
            else -> movie
                .copy(
                    isPopularMovie = true,
                    posterImgURL = APIProvider().getImageMovieBaseUrl(movie.posterImg?: ""),
                    backdropPosterImgURL = APIProvider().getImageMovieBaseUrl(movie.backdropPosterImgURL?: "")
                )
        }
    }
}