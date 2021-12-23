package com.daveloper.moviesapp.ui.viewmodel.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daveloper.moviesapp.auxiliar.internet_connection.InternetConnectionHelper
import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.domain.GetPopularMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val internetConnectionHelper: InternetConnectionHelper,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
): ViewModel() {
    // LIVE DATA VARS
    // Show info msg
    private val _showInfoMessageFromResource = MutableLiveData<Int>()
    val showInfoMessageFromResource : LiveData<Int> get() = _showInfoMessageFromResource
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
    // ProgressBar
    // Popular Movies
    private val _progressPopularMoviesVisibility = MutableLiveData<Boolean>()
    val progressPopularMoviesVisibility : LiveData<Boolean> get() = _progressPopularMoviesVisibility
    // User Favorite Movies
    private val _progressUserFavoriteMoviesVisibility = MutableLiveData<Boolean>()
    val progressUserFavoriteMoviesVisibility : LiveData<Boolean> get() = _progressUserFavoriteMoviesVisibility
    // Now Playing Movies
    private val _progressNowPlayingMoviesVisibility = MutableLiveData<Boolean>()
    val progressNowPlayingMoviesVisibility : LiveData<Boolean> get() = _progressNowPlayingMoviesVisibility
    // Upcoming Movies
    private val _progressUpcomingMoviesVisibility = MutableLiveData<Boolean>()
    val progressUpcomingMoviesVisibility : LiveData<Boolean> get() = _progressUpcomingMoviesVisibility


    fun onCreate() {
        _refreshVisibility.value = false
        // We make sure it doesn't just go to the other Fragment
        _goToMovieInfoFragment.value = -1
        viewModelScope.launch {
            getDataToFillPopularMoviesRecyclerView()
        }
    }

    // Popular Movies
    private suspend fun getDataToFillPopularMoviesRecyclerView (
        refresh: Boolean = false
    ) {
        try {
            // Show the progress bar
            _progressPopularMoviesVisibility.postValue(true)
            // Get the internet state of the device
            val internetConnectionState = internetConnectionHelper.internetIsConnected()
            // Get a list of Popular Movies from the model via Use Case
            val popularMovies: List<Movie> = getPopularMoviesUseCase.getData(internetConnectionState, refresh)
            // Verify if the list of movies is null or empty
            if (!popularMovies.isNullOrEmpty()) {
                // Send the info to fill the recyclerView
                _popularMoviesData.postValue(popularMovies)
            } else {
                // Send a empty List to the recyclerView
                _popularMoviesData.postValue(emptyList())
                // TODO msg to the user
            }
        } catch (e: Exception) {
            // Send a empty List to the recyclerView
            _popularMoviesData.postValue(emptyList())
            //TODO msg to the user
        } finally {
            // Hide the progress bar
            _progressPopularMoviesVisibility.postValue(false)
        }
    }
    // User Favorite Movies

    // Now Playing Movies

    // Upcoming Movies

    fun onMovieClicked(movieIDSelected: String) {
        _goToMovieInfoFragment.value = movieIDSelected.toInt()
    }

    fun onRefresh() {
        viewModelScope.launch {
            getDataToFillPopularMoviesRecyclerView(true)
        }
        _refreshVisibility.value = false
    }

    fun navigationCompleted() {
        _goToMovieInfoFragment.value = null
    }


}