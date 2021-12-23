package com.daveloper.moviesapp.ui.viewmodel.movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daveloper.moviesapp.R
import com.daveloper.moviesapp.auxiliar.internet_connection.InternetConnectionHelper
import com.daveloper.moviesapp.core.ResourceProvider
import com.daveloper.moviesapp.data.model.entity.Actor
import com.daveloper.moviesapp.data.model.entity.Genre
import com.daveloper.moviesapp.data.model.entity.Video
import com.daveloper.moviesapp.domain.AddOrRemoveMovieFromFavoritesUseCase
import com.daveloper.moviesapp.domain.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val internetConnectionHelper: InternetConnectionHelper,
    private val resourceProvider: ResourceProvider,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val addOrRemoveMovieFromFavoritesUseCase: AddOrRemoveMovieFromFavoritesUseCase
): ViewModel() {
    // LIVE DATA VARS
    // Progress bar
    private val _setProgressVisibility = MutableLiveData<Boolean>()
    val setProgressVisibility : LiveData<Boolean> get() = _setProgressVisibility
    // Show info msg
    private val _showInfoMessage = MutableLiveData<String>()
    val showInfoMessage : LiveData<String> get() = _showInfoMessage
    // Navigation
    private val _goToMoviesFragment = MutableLiveData<Boolean?>()
    val goToMoviesFragment : LiveData<Boolean?> get() = _goToMoviesFragment
    // Refreshing
    private val _refreshVisibility = MutableLiveData<Boolean>()
    val refreshVisibility : LiveData<Boolean> get() = _refreshVisibility
    /// To fill data
    // Movie name on toolbar
    private val _setToolbarTitleText = MutableLiveData<String>()
    val setToolbarTitleText : LiveData<String> get() = _setToolbarTitleText
    // Add movie to favorite state
    private val _setFavButtonIcon = MutableLiveData<Int>()
    val setFavButtonIcon : LiveData<Int> get() = _setFavButtonIcon
    // Movie name
    private val _setTitleText = MutableLiveData<String>()
    val setTitleText : LiveData<String> get() = _setTitleText
    // Release date
    private val _setDateText = MutableLiveData<String>()
    val setDateText : LiveData<String> get() = _setDateText
    // Adult class
    private val _setAdultContentVisibility = MutableLiveData<Boolean>()
    val setAdultContentVisibility : LiveData<Boolean> get() = _setAdultContentVisibility
    // Genres Chips
    private val _setGenreChipData = MutableLiveData<List<Genre>>()
    val setGenreChipData : LiveData<List<Genre>> get() = _setGenreChipData
    // Video id
    private val _setVideoYoutubeId = MutableLiveData<String>()
    val setVideoYoutubeId : LiveData<String> get() = _setVideoYoutubeId
    // Video internet error
    private val _setYoutubeVideoErrorVisibility = MutableLiveData<Boolean>()
    val setYoutubeVideoErrorVisibility : LiveData<Boolean> get() = _setYoutubeVideoErrorVisibility
    // Poster img
    private val _setPosterImgUrl = MutableLiveData<String>()
    val setPosterImgUrl : LiveData<String> get() = _setPosterImgUrl
    // Overview
    private val _setOverviewText = MutableLiveData<String>()
    val setOverviewText : LiveData<String> get() = _setOverviewText
    // Rating
    private val _setRatingBarValue = MutableLiveData<Float>()
    val setRatingBarValue : LiveData<Float> get() = _setRatingBarValue
    private val _setRatingText = MutableLiveData<String>()
    val setRatingText : LiveData<String> get() = _setRatingText
    // RecyclerView Data
    // Movie Cast
    private val _movieCastData = MutableLiveData<List<Actor>>()
    val movieCastData : LiveData<List<Actor>> get() = _movieCastData

    fun onCreate(movieIdSelected: Int) {
        _setAdultContentVisibility.value = false
        _goToMoviesFragment.value = false
        _refreshVisibility.value = false
        viewModelScope.launch {
            fillInitData(movieIdSelected, false)
        }
    }

    private suspend fun fillInitData (
        movieIdSelected: Int,
        refresh: Boolean
    ) {
        try {
            _setProgressVisibility.postValue(true)
            // Get the internet state of the device
            val internetConnectionState = internetConnectionHelper.internetIsConnected()
            val data = getMovieDetailsUseCase.getData(movieIdSelected, internetConnectionState, refresh)
            if (data != null) {
                // Fill data
                if (data.isUserFavoriteMovie) {
                    _setFavButtonIcon.postValue(R.drawable.ic_fav_heart)
                } else {
                    _setFavButtonIcon.postValue(R.drawable.ic_nofav_heart)
                }
                // Movie title
                if (!data.name.isNullOrEmpty()) {
                    _setToolbarTitleText.postValue(data.name!!)
                    _setTitleText.postValue(data.name!!)
                } else {
                    _setToolbarTitleText.postValue("")
                    _setTitleText.postValue("")
                }
                // Release date
                if (!data.releaseDate.isNullOrEmpty()) {
                    _setDateText.postValue(data.releaseDate!!)
                } else {
                    _setDateText.postValue("")
                }
                // Adult content
                if (data.adultClassification == true){
                    _setAdultContentVisibility.postValue(true)
                } else {
                    _setAdultContentVisibility.postValue(false)
                }
                // Video id
                if (!data.videos.isNullOrEmpty()){
                    if (internetConnectionState) {
                        _setVideoYoutubeId.postValue(getVideoTrailerID(data.videos!!))
                        _setYoutubeVideoErrorVisibility.postValue(false)
                    } else {
                        _setYoutubeVideoErrorVisibility.postValue(true)
                    }
                }
                // Genres Chips
                if (!data.genres.isNullOrEmpty()){
                    _setGenreChipData.postValue(data.genres!!)
                } else {
                    _setGenreChipData.postValue(emptyList())
                }

                // Poster img
                if (!data.posterImgURL.isNullOrEmpty()){
                    _setPosterImgUrl.postValue(data.posterImgURL!!)
                }
                // Overview
                if (!data.overview.isNullOrEmpty()){
                    _setOverviewText.postValue(data.overview!!)
                } else {
                    _setOverviewText.postValue("")
                }
                // Rating
                if (!data.rating.isNullOrEmpty()){
                    _setRatingText.postValue(
                        data.rating!!
                                + " "
                                + resourceProvider.getStringResource(R.string.tV_movieDT_rating_div))
                    _setRatingBarValue.postValue(data.rating!!.toFloat())
                }
                // RecyclerView Data
                // Movie Cast
                if (!data.cast.isNullOrEmpty()){
                    _movieCastData.postValue(data.cast!!)
                } else {
                    _movieCastData.postValue(emptyList())
                }
            }

        } catch (e: Exception) {
            // msg error to user
            _showInfoMessage.postValue(resourceProvider.getStringResource(R.string.msg_general_error))
            // come back to movies fragment
            _goToMoviesFragment.postValue(true)

        } finally {
            _setProgressVisibility.postValue(false)

        }
    }

    private fun getVideoTrailerID(videos: List<Video>): String {
        var idVideo = ""
        for (video in videos){
            if (video.isOfficial == true && video.type.equals("Trailer")) {
                idVideo = video.keyFromVideo.toString()
                break
            }
        }
        return if (idVideo.isNotEmpty()) {
            idVideo
        } else {
            videos.get(0).keyFromVideo!!
        }
    }

    fun onRefresh(movieIdSelected: Int) {
        viewModelScope.launch {
            fillInitData(movieIdSelected, true)
            _refreshVisibility.value = false
        }
    }

    fun onBackClicked() {
        _goToMoviesFragment.value = true
    }

    fun navigationComplete() {
        _goToMoviesFragment.value = null
    }

    fun onFavoriteClicked(movieIdSelected: Int) {
        viewModelScope.launch {
            try {
                val movie = addOrRemoveMovieFromFavoritesUseCase.addOrRemove(movieIdSelected)
                if (movie != null) {
                    // msg to user
                    if (movie.isUserFavoriteMovie) {
                        _showInfoMessage.postValue("!${movie.name} ${resourceProvider.getStringResource(R.string.msg_favorite_movie_added)}")
                    } else {
                        _showInfoMessage.postValue("!${movie.name} ${resourceProvider.getStringResource(R.string.msg_favorite_movie_removed)}")
                    }
                }
            } catch (e: Exception) {
                // msg error to user
                _showInfoMessage.postValue(resourceProvider.getStringResource(R.string.msg_general_error))
            } finally {
                onRefresh(movieIdSelected)
            }

        }
    }
}