package com.daveloper.moviesapp.ui.viewmodel.search_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daveloper.moviesapp.R
import com.daveloper.moviesapp.auxiliar.internet_connection.InternetConnectionHelper
import com.daveloper.moviesapp.core.ResourceProvider
import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.domain.GetMoviesFromSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMovieViewModel @Inject constructor(
    private val internetConnectionHelper: InternetConnectionHelper,
    private val resourceProvider: ResourceProvider,
    private val getMoviesFromSearchUseCase: GetMoviesFromSearchUseCase
): ViewModel() {

    // LIVE DATA VARS
    // Progress bar
    private val _setProgressVisibility = MutableLiveData<Boolean>()
    val setProgressVisibility : LiveData<Boolean> get() = _setProgressVisibility
    // Show info msg
    private val _showInfoMessage = MutableLiveData<String>()
    val showInfoMessage : LiveData<String> get() = _showInfoMessage
    // Navigation
    private val _goToMovieInfoFragment = MutableLiveData<Int?>()
    val goToMovieInfoFragment : LiveData<Int?> get() = _goToMovieInfoFragment
    // Refreshing
    private val _refreshVisibility = MutableLiveData<Boolean>()
    val refreshVisibility : LiveData<Boolean> get() = _refreshVisibility
    // RecyclerView Data
    // Movies found
    private val _moviesFoundData = MutableLiveData<List<Movie>>()
    val moviesFoundData : LiveData<List<Movie>> get() = _moviesFoundData
    // setText
    private val _setRVText = MutableLiveData<String>()
    val setRVText : LiveData<String> get() = _setRVText
    // visibility
    private val _setRVTextVisibility = MutableLiveData<Boolean>()
    val setRVTextVisibility : LiveData<Boolean> get() = _setRVTextVisibility



    // Search Movies
    // public
    fun searchMovies (
        keyToFind: String,
        refresh: Boolean = false
    ) {
        viewModelScope.launch {
            getDataToFillSearchMoviesRecyclerView(keyToFind, refresh)
        }
    }
    // private
    private suspend fun getDataToFillSearchMoviesRecyclerView (
        keyToFind: String,
        refresh: Boolean = false
    ) {
        try {
            if (keyToFind.isNotEmpty()) {
                // Visibility text off
                _setRVTextVisibility.postValue(false)
                // Show the progress bar
                _setProgressVisibility.postValue(true)
                // Get the internet state of the device
                val internetConnectionState = internetConnectionHelper.internetIsConnected()
                // Get a list of Movies Found from the model via Use Case
                val moviesFound: List<Movie> = getMoviesFromSearchUseCase.getData(keyToFind, internetConnectionState, refresh)
                // Verify if the list of movies is null or empty
                if (!moviesFound.isNullOrEmpty()) {
                    // Send the info to fill the recyclerView
                    _moviesFoundData.postValue(moviesFound)
                } else {
                    // Send a empty List to the recyclerView
                    _moviesFoundData.postValue(emptyList())
                    // Msg to the user
                    _setRVText.postValue(
                        resourceProvider.getStringResource(R.string.tV_no_movies_found_for_api_not_found)
                    )
                    // Visibility text on
                    _setRVTextVisibility.postValue(true)
                }
            } else {
                // Msg to the user
                _setRVText.postValue(
                    resourceProvider.getStringResource(R.string.tV_init_movies_found)
                )
                // Visibility text on
                _setRVTextVisibility.postValue(true)
            }

        } catch (e: Exception) {
            // Send a empty List to the recyclerView
                _moviesFoundData.postValue(emptyList())
            // Msg to the user
            if (internetConnectionHelper.internetIsConnected()) {
                // If internet connection is true -> no results found
                _setRVText.postValue(
                    resourceProvider.getStringResource(R.string.tV_no_movies_found_for_api_not_found)
                )
            } else {
                // If internet connection is false -> user need to activate internet
                _setRVText.postValue(
                    resourceProvider.getStringResource(R.string.tV_no_movies_found_for_no_internet_connection)
                )
            }
            // Visibility text on
            _setRVTextVisibility.postValue(true)
        } finally {
            // Hide the progress bar
            _setProgressVisibility.postValue(false)
        }

    }

    fun navigationCompleted() {
        _goToMovieInfoFragment.value = null
    }

    fun onRefresh(
        keyToFind: String,
    ) {
        viewModelScope.launch {
            getDataToFillSearchMoviesRecyclerView(keyToFind, true)
            _refreshVisibility.value = false
        }
    }

    fun onMovieClicked(movieIDSelected: String) {
        _goToMovieInfoFragment.value = movieIDSelected.toInt()
    }

}