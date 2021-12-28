package com.daveloper.moviesapp.ui.viewmodel.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daveloper.moviesapp.auxiliar.internet_connection.InternetConnectionHelper
import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.domain.GetNowPlayingMoviesUseCase
import com.daveloper.moviesapp.domain.GetPopularMoviesUseCase
import com.daveloper.moviesapp.domain.GetUpcomingMoviesUseCase
import com.daveloper.moviesapp.domain.GetUserFavoriteMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val internetConnectionHelper: InternetConnectionHelper,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getUserFavoriteMoviesUseCase: GetUserFavoriteMoviesUseCase,
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase
): ViewModel() {
    // LIVE DATA VARS
    // Progress bar
    private val _setProgressVisibility = MutableLiveData<Boolean>()
    val setProgressVisibility : LiveData<Boolean> get() = _setProgressVisibility
    // Show info msg

    // Navigation
    private val _goToMovieInfoFragment = MutableLiveData<Int?>()
    val goToMovieInfoFragment : LiveData<Int?> get() = _goToMovieInfoFragment
    // Refreshing
    private val _refreshVisibility = MutableLiveData<Boolean>()
    val refreshVisibility : LiveData<Boolean> get() = _refreshVisibility
    // Recycler View DATA
    // Popular Movies
    private val _popularMoviesData = MutableLiveData<List<Movie>>()
    val popularMoviesData : LiveData<List<Movie>> get() = _popularMoviesData
    // User Favorite Movies
    private val _userFavoriteMoviesData = MutableLiveData<List<Movie>>()
    val userFavoriteMoviesData : LiveData<List<Movie>> get() = _userFavoriteMoviesData
    // Now Playing Movies
    private val _nowPlayingMoviesData = MutableLiveData<List<Movie>>()
    val nowPlayingMoviesData : LiveData<List<Movie>> get() = _nowPlayingMoviesData
    // Upcoming Movies
    private val _upcomingMoviesData = MutableLiveData<List<Movie>>()
    val upcomingMoviesData : LiveData<List<Movie>> get() = _upcomingMoviesData
    // No popular movies msg
    private val _setNoPopularMoviesVisibility = MutableLiveData<Boolean>()
    val setNoPopularMoviesVisibility : LiveData<Boolean> get() = _setNoPopularMoviesVisibility
    // No favorite movies msg
    private val _setNoFavoriteMoviesVisibility = MutableLiveData<Boolean>()
    val setNoFavoriteMoviesVisibility : LiveData<Boolean> get() = _setNoFavoriteMoviesVisibility
    // No now playing movies
    private val _setNoNowPlayingMoviesVisibility = MutableLiveData<Boolean>()
    val setNoNowPlayingMoviesVisibility : LiveData<Boolean> get() = _setNoNowPlayingMoviesVisibility
    // No upcoming movies
    private val _setNoUpcomingMoviesVisibility = MutableLiveData<Boolean>()
    val setNoUpcomingMoviesVisibility : LiveData<Boolean> get() = _setNoUpcomingMoviesVisibility

    fun onCreate() {
        _refreshVisibility.value = false
        // We make sure it doesn't just go to the other Fragment
        _goToMovieInfoFragment.value = null
        viewModelScope.launch {
            getDataToFillPopularMoviesRecyclerView()
            getDataToFillUserFavoriteMoviesRecyclerView()
            getDataToFillNowPlayingMoviesRecyclerView()
            getDataToFillUpcomingMoviesRecyclerView()
        }
    }

    // Popular Movies
    private suspend fun getDataToFillPopularMoviesRecyclerView (
        refresh: Boolean = false
    ) {
        try {
            // Show the progress bar
            _setProgressVisibility.postValue(true)
            // Get the internet state of the device
            val internetConnectionState = internetConnectionHelper.internetIsConnected()
            // Get a list of Popular Movies from the model via Use Case
            val popularMovies: List<Movie> = getPopularMoviesUseCase.getData(internetConnectionState, refresh)
            // Verify if the list of movies is null or empty
            if (!popularMovies.isNullOrEmpty()) {
                // Send the info to fill the recyclerView
                _popularMoviesData.postValue(popularMovies)
                // msg to the user
                _setNoPopularMoviesVisibility.postValue(false)
            } else {
                // Send a empty List to the recyclerView
                _popularMoviesData.postValue(emptyList())
                // msg to the user
                _setNoPopularMoviesVisibility.postValue(true)
            }
        } catch (e: Exception) {
            // Send a empty List to the recyclerView
            _popularMoviesData.postValue(emptyList())
            // msg to the user
            _setNoPopularMoviesVisibility.postValue(true)
        } finally {
            // Hide the progress bar
            _setProgressVisibility.postValue(false)
        }
    }
    // User Favorite Movies
    private suspend fun getDataToFillUserFavoriteMoviesRecyclerView() {
        try {
            // Show the progress bar
            _setProgressVisibility.postValue(true)
            // Get a list of User Favorite Movies from the model via Use Case
            val userFavoriteMovies: List<Movie> = getUserFavoriteMoviesUseCase.getData()
            // Verify if the list of movies is null or empty
            if (!userFavoriteMovies.isNullOrEmpty()) {
                // Send the info to fill the recyclerView
                _userFavoriteMoviesData.postValue(userFavoriteMovies)
                _setNoFavoriteMoviesVisibility.postValue(false)
            } else {
                // Send a empty List to the recyclerView
                _userFavoriteMoviesData.postValue(emptyList())
                // Msg to the user
                _setNoFavoriteMoviesVisibility.postValue(true)
            }

        } catch (e: Exception) {
            // Send a empty List to the recyclerView
            _userFavoriteMoviesData.postValue(emptyList())
            // Msg to the user
            _setNoFavoriteMoviesVisibility.postValue(true)
        } finally {
            _setProgressVisibility.postValue(false)
        }
    }

    // Now Playing Movies
    private suspend fun getDataToFillNowPlayingMoviesRecyclerView (
        refresh: Boolean = false
    ) {
        try {
            // Show the progress bar
            _setProgressVisibility.postValue(true)
            // Get the internet state of the device
            val internetConnectionState = internetConnectionHelper.internetIsConnected()
            // Get a list of Popular Movies from the model via Use Case
            val nowPlayingMovies: List<Movie> = getNowPlayingMoviesUseCase.getData(internetConnectionState, refresh)
            // Verify if the list of movies is null or empty
            if (!nowPlayingMovies.isNullOrEmpty()) {
                // Send the info to fill the recyclerView
                _nowPlayingMoviesData.postValue(nowPlayingMovies)
                // msg to the user
                _setNoNowPlayingMoviesVisibility.postValue(false)
            } else {
                // Send a empty List to the recyclerView
                _nowPlayingMoviesData.postValue(emptyList())
                // msg to the user
                _setNoNowPlayingMoviesVisibility.postValue(true)
            }
        } catch (e: Exception) {
            // Send a empty List to the recyclerView
            _nowPlayingMoviesData.postValue(emptyList())
            // msg to the user
            _setNoNowPlayingMoviesVisibility.postValue(true)
        } finally {
            // Hide the progress bar
            _setProgressVisibility.postValue(false)
        }
    }

    // Upcoming Movies
    private suspend fun getDataToFillUpcomingMoviesRecyclerView (
        refresh: Boolean = false
    ) {
        try {
            // Show the progress bar
            _setProgressVisibility.postValue(true)
            // Get the internet state of the device
            val internetConnectionState = internetConnectionHelper.internetIsConnected()
            // Get a list of Popular Movies from the model via Use Case
            val upcomingMovies: List<Movie> = getUpcomingMoviesUseCase.getData(internetConnectionState, refresh)
            // Verify if the list of movies is null or empty
            if (!upcomingMovies.isNullOrEmpty()) {
                // Send the info to fill the recyclerView
                _upcomingMoviesData.postValue(upcomingMovies)
                // msg to the user
                _setNoUpcomingMoviesVisibility.postValue(false)
            } else {
                // Send a empty List to the recyclerView
                _upcomingMoviesData.postValue(emptyList())
                // msg to the user
                _setNoUpcomingMoviesVisibility.postValue(true)
            }
        } catch (e: Exception) {
            // Send a empty List to the recyclerView
            _upcomingMoviesData.postValue(emptyList())
            // msg to the user
            _setNoUpcomingMoviesVisibility.postValue(true)
        } finally {
            // Hide the progress bar
            _setProgressVisibility.postValue(false)
        }
    }

    fun onMovieClicked(movieIDSelected: String) {
        _goToMovieInfoFragment.value = movieIDSelected.toInt()
    }

    fun onRefresh() {
        viewModelScope.launch {
            getDataToFillPopularMoviesRecyclerView(true)
            getDataToFillUserFavoriteMoviesRecyclerView()
            getDataToFillNowPlayingMoviesRecyclerView(true)
            getDataToFillUpcomingMoviesRecyclerView(true)
            _refreshVisibility.value = false
        }
    }

    fun navigationCompleted() {
        _goToMovieInfoFragment.value = null
    }
}