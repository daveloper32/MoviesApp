package com.daveloper.moviesapp.ui.view.movie_details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.daveloper.moviesapp.auxiliar.ext_fun.activity_context.addChip
import com.daveloper.moviesapp.auxiliar.ext_fun.activity_context.loadImage
import com.daveloper.moviesapp.auxiliar.ext_fun.activity_context.toast
import com.daveloper.moviesapp.data.model.entity.Actor
import com.daveloper.moviesapp.databinding.FragmentMovieDetailsBinding
import com.daveloper.moviesapp.ui.view.movie_details.adapters.ActorAdapter
import com.daveloper.moviesapp.ui.viewmodel.movie_details.MovieDetailsViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint

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
    private lateinit var movieCastAdapter: ActorAdapter

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
        binding.rVMovieCast.layoutManager =
            LinearLayoutManager(
                this.requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        // onCreate viewModel
        viewModel.onCreate(movieIdNavArgs.movieIdSelected)
        // Listeners
        binding.tBMovies.tBImgVTeamDetBackicon.setOnClickListener(this)
        binding.tBMovies.bEditPerfilTBFavMovie.setOnClickListener(this)
        binding.refreshMovieInfo.setOnRefreshListener(this)
        // Observer to the youtube video, in order to auto adapt the video to the lifecycle changes of the fragment
        viewLifecycleOwner.lifecycle.addObserver(binding.yPVideo)
        // Setting the rating bar to not be clickable by the user
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.rBMovieDtRating.focusable = View.NOT_FOCUSABLE
        }
    }

    private fun initLiveData() {
        //// Progress
        viewModel.setProgressVisibility.observe(
            this,
            Observer {
                if (it) {
                    binding.pgsBMovieDetails.visibility = View.VISIBLE
                } else {
                    binding.pgsBMovieDetails.visibility = View.GONE
                }
            }
        )
        //// Info msg
        viewModel.showInfoMessage.observe(
            this,
            Observer {
                this.requireActivity().toast(it)
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
        // Add movie to favorite state
        viewModel.setFavButtonIcon.observe(
            this,
            Observer {
                binding.tBMovies.bEditPerfilTBFavMovie.setImageResource(it)
            }
        )
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
        // Video internet error
        viewModel.setYoutubeVideoErrorVisibility.observe(
            this,
            Observer {
                if (it) {
                    binding.tVYoutubeVideoMSG.visibility = View.VISIBLE
                } else {
                    binding.tVYoutubeVideoMSG.visibility = View.GONE
                }
            }
        )
        // Genres Chips
        viewModel.setGenreChipData.observe(
            this,
            Observer { genres ->
                // Delete all the chips on the ChipGroup
                binding.cGGenres.removeAllViews()
                // Adding the chips
                genres.forEach { genre ->
                    genre.name?.let {
                        binding.cGGenres.addChip(
                            this.requireContext(),
                            it
                        )
                    }
                }
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
        // Movie Cast
        viewModel.movieCastData.observe(
            this,
            Observer {
                sendMovieCastToAdapter(it)
            }
        )
    }

    // Movie Cast
    private fun sendMovieCastToAdapter (
        castList: List<Actor>
    ) {
        movieCastAdapter = ActorAdapter(
            castList
        )
        binding.rVMovieCast.adapter = movieCastAdapter
    }

    override fun onRefresh() {
        viewModel.onRefresh(movieIdNavArgs.movieIdSelected)
    }

    override fun onClick(v: View?) {
        val idSelected: Int = v!!.id
        when (idSelected) {
            // Toolbar
            binding.tBMovies.tBImgVTeamDetBackicon.id -> viewModel.onBackClicked()
            // Favorite Movie
            binding.tBMovies.bEditPerfilTBFavMovie.id -> viewModel.onFavoriteClicked(movieIdNavArgs.movieIdSelected)
        }
    }
}