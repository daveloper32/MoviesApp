package com.daveloper.moviesapp.ui.view.movie_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.daveloper.moviesapp.R
import com.daveloper.moviesapp.auxiliar.ext_fun.activity_context.getStringResource
import com.daveloper.moviesapp.auxiliar.ext_fun.activity_context.toast
import com.daveloper.moviesapp.databinding.FragmentMovieDetailsBinding
import com.daveloper.moviesapp.ui.view.movies.MoviesFragmentDirections
import com.daveloper.moviesapp.ui.viewmodel.movie_details.MovieDetailsViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

import androidx.annotation.NonNull
import com.daveloper.moviesapp.auxiliar.ext_fun.activity_context.loadImage

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener




@AndroidEntryPoint
class MovieDetailsFragment : Fragment(),
    View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener
{

    // Init Vars
    // Binding
    private lateinit var binding: FragmentMovieDetailsBinding
    // Safe Args reference
    private val movieIdNavArgs by navArgs<MovieDetailsFragmentArgs>()
    // ViewModel
    private  val viewModel: MovieDetailsViewModel by viewModels<MovieDetailsViewModel>()
    // Adapters RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailsBinding.inflate(inflater)
        initView()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initView() {
        // init LiveData Observers
        initLiveData()
        // Layout managers for each recyclerView

        // onCreate viewModel
        viewModel.onCreate(movieIdNavArgs.movieIdSelected)
        // Listeners
        binding.tBMovies.tBImgVTeamDetBackicon.setOnClickListener(this)
        binding.refreshMovieInfo.setOnRefreshListener(this)
        // Observer to the youtube video, in order to auto adapt the video to the lifecycle changes of the fragment
        viewLifecycleOwner.lifecycle.addObserver(binding.yPVideo)
    }

    private fun initLiveData() {
        //// Info msg
        viewModel.showInfoMessageFromResource.observe(
            this,
            Observer {
                this.requireActivity().toast(
                    this.requireActivity().getStringResource(it)
                )
            }
        )
        //// Navigation
        viewModel.goToMoviesFragment.observe(
            this,
            Observer {
                // Go to the Movie Fragment
                if (it == true) {
                    findNavController()
                        .navigate(
                            MovieDetailsFragmentDirections
                                .actionMovieDetailsFragmentToMoviesFragment()
                        )
                    viewModel.navigationComplete()
                }
            }
        )
        //// Refresh
        viewModel.refreshVisibility.observe(
            this,
            Observer {
                binding.refreshMovieInfo.isRefreshing = it
            }
        )
        /// Fill data
        // Movie name on toolbar
        viewModel.setToolbarTitleText.observe(
            this,
            Observer {
                binding.tBMovies.tVEditPerfilTBTitle.text = it
            }
        )
        // Movie name
        viewModel.setTitleText.observe(
            this,
            Observer {
                binding.tVMoviecVTitle.text = it
            }
        )
        // Release date
        viewModel.setDateText.observe(
            this,
            Observer {
                binding.tVMoviecVDate.text = it
            }
        )
        // Adult class
        viewModel.setAdultContentVisibility.observe(
            this,
            Observer {
                if (it) {
                    binding.tVMoviecV18Content.visibility = View.VISIBLE
                } else {
                    binding.tVMoviecV18Content.visibility = View.GONE
                }
            }
        )
        // Video id
        viewModel.setVideoYoutubeId.observe(
            this,
            Observer {
                binding.yPVideo.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(it, 0f)
                    }
                })
            }
        )
        // Poster img
        viewModel.setPosterImgUrl.observe(
            this,
            Observer {
                binding.imgVMovieDtPoster.loadImage(it, false)
            }
        )
        // Overview
        viewModel.setOverviewText.observe(
            this,
            Observer {
                binding.tVOverview.text = it
            }
        )
        // Rating
        viewModel.setRatingText.observe(
            this,
            Observer {
                binding.tVMovieDtRating.text = it
            }
        )
        viewModel.setRatingBarValue.observe(
            this,
            Observer {
                binding.rBMovieDtRating.rating = it
            }
        )
        //// RecyclerView Data
    }

    override fun onRefresh() {
        viewModel.onRefresh(movieIdNavArgs.movieIdSelected)
    }

    override fun onClick(v: View?) {
        val idSelected: Int = v!!.id
        when (idSelected) {
            // Toolbar
            binding.tBMovies.tBImgVTeamDetBackicon.id -> viewModel.onBackClicked()
        }
    }

}