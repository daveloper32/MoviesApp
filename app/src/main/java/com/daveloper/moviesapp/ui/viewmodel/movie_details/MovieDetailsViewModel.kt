package com.daveloper.moviesapp.ui.viewmodel.movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daveloper.moviesapp.R
import com.daveloper.moviesapp.auxiliar.internet_connection.InternetConnectionHelper
import com.daveloper.moviesapp.core.ResourceProvider
import com.daveloper.moviesapp.data.model.entity.Video
import com.daveloper.moviesapp.domain.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val internetConnectionHelper: InternetConnectionHelper,
    private val resourceProvider: ResourceProvider,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
): ViewModel() {
    // LIVE DATA VARS
    // Show info msg
    private val _showInfoMessageFromResource = MutableLiveData<Int>()
    val showInfoMessageFromResource : LiveData<Int> get() = _showInfoMessageFromResource
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
    // Movie name
    private val _setTitleText = MutableLiveData<String>()
    val setTitleText : LiveData<String> get() = _setTitleText
    // Release date
    private val _setDateText = MutableLiveData<String>()
    val setDateText : LiveData<String> get() = _setDateText
    // Adult class
    private val _setAdultContentVisibility = MutableLiveData<Boolean>()
    val setAdultContentVisibility : LiveData<Boolean> get() = _setAdultContentVisibility
    // Video id
    private val _setVideoYoutubeId = MutableLiveData<String>()
    val setVideoYoutubeId : LiveData<String> get() = _setVideoYoutubeId
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

    fun onCreate(movieIdSelected: Int) {
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
            // Get the internet state of the device
            val internetConnectionState = internetConnectionHelper.internetIsConnected()
            val data = getMovieDetailsUseCase.getData(movieIdSelected, internetConnectionState, refresh)
            if (data != null) {
                // Fill data
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
                    _setVideoYoutubeId.postValue(getVideoTrailerID(data.videos!!))
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
                    _setRatingBarValue.postValue(getRatingValueIn5Scale(data.rating!!))
                }

            }

        } catch (e: Exception) {

        } finally {

        }
    }

    private fun getRatingValueIn5Scale(rating: String): Float {
        return (rating.toFloat()*5) / 10
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
        }
        _refreshVisibility.value = false
    }

    fun onBackClicked() {
        _goToMoviesFragment.value = true
    }

    fun navigationComplete() {
        _goToMoviesFragment.value = null
    }
}