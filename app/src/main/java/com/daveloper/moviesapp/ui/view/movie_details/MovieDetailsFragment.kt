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
import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.data.model.entity.Review
import com.daveloper.moviesapp.databinding.FragmentMovieDetailsBinding
import com.daveloper.moviesapp.ui.view.movie_details.adapters.ActorAdapter
import com.daveloper.moviesapp.ui.view.movie_details.adapters.ReviewAdapter
import com.daveloper.moviesapp.ui.view.movies.adapters.MovieAdapter
import com.daveloper.moviesapp.ui.viewmodel.movie_details.MovieDetailsViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(),
    View.OnClickListener,
    MovieAdapter.OnItemClickListener,
    ReviewAdapter.OnItemClickListener,
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
    // Movie Cast
    private lateinit var movieCastAdapter: ActorAdapter
    // Similar Movies
    private lateinit var similarMoviesAdapter: MovieAdapter
    // Movie Review
    private lateinit var movieReviewsAdapter: ReviewAdapter

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
        // Movie Cast
        binding.rVMovieCast.layoutManager =
            LinearLayoutManager(
                this.requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        // Similar Movies
        binding.rVSimilarMovies.layoutManager =
            LinearLayoutManager(
                this.requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        // Movie Reviews
        binding.rVMovieReviews.layoutManager =
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
        // Go back to Movies Fragment
        viewModel.goToMoviesFragment.observe(
            this,
            Observer {
                if (it == true) {
                    this.requireActivity().onBackPressedDispatcher.onBackPressed()
                    viewModel.navigationComplete()
                }

                // Go to the Movie Fragment
                /*if (it == true) {
                    findNavController()
                        .navigate(
                            MovieDetailsFragmentDirections
                                .actionMovieDetailsFragmentToMoviesFragment()
                        )
                    viewModel.navigationComplete()
                }*/
            }
        )
        // Charge Similar Movie Info on the same Fragment
        viewModel.goToNewMovieInfoFragment.observe(
            this,
            Observer {
                // Go to the same fragment with other movie info
                if (it != null) {
                    if (it>0) {
                        findNavController()
                            .navigate(
                                MovieDetailsFragmentDirections
                                    .actionMovieDetailsFragmentSelf(it)
                            )
                        viewModel.navigationSimilarMovieCompleted()
                    }
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

                    override fun onError(
                        youTubePlayer: YouTubePlayer,
                        error: PlayerConstants.PlayerError
                    ) {
                        youTubePlayer.loadVideo(it, 0f)
                        super.onError(youTubePlayer, error)
                    }
                })
            }
        )
        // Video visibility
        viewModel.setVideoYoutubeVisibility.observe(
            this,
            Observer {
                if (it) {
                    binding.yPVideo.visibility = View.VISIBLE
                } else {
                    binding.yPVideo.visibility = View.GONE
                }
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
        // Movie Cast internet error
        viewModel.setMovieCastDataErrorVisibility.observe(
            this,
            Observer {
                if (it) {
                    binding.tVNoMovieCastFound.visibility = View.VISIBLE
                } else {
                    binding.tVNoMovieCastFound.visibility = View.GONE
                }
            }
        )
        // Movie Cast
        viewModel.movieCastData.observe(
            this,
            Observer {
                sendMovieCastToAdapter(it)
            }
        )
        // Similar Movies internet error
        viewModel.setSimilarMoviesDataErrorVisibility.observe(
            this,
            Observer {
                if (it) {
                    binding.tVNoSimilarMoviesFound.visibility = View.VISIBLE
                } else {
                    binding.tVNoSimilarMoviesFound.visibility = View.GONE
                }
            }
        )
        // Similar Movies
        viewModel.similarMoviesData.observe(
            this,
            Observer {
                sendSimilarMoviesToAdapter(it)
            }
        )
        // Movie Reviews internet error
        viewModel.setMovieReviewsDataErrorVisibility.observe(
            this,
            Observer {
                if (it) {
                    binding.tVNoMovieReviewsFound.visibility = View.VISIBLE
                } else {
                    binding.tVNoMovieReviewsFound.visibility = View.GONE
                }
            }
        )
        // Movie Reviews
        viewModel.movieReviewsData.observe(
            this,
            Observer {
                sendMovieReviewsToAdapter(it)
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

    // Similar Movies
    private fun sendSimilarMoviesToAdapter (
        similarMovies: List<Movie>
    ) {
        similarMoviesAdapter = MovieAdapter(
            similarMovies,
            this
        )
        binding.rVSimilarMovies.adapter = similarMoviesAdapter
    }

    // Movie Reviews
    private fun sendMovieReviewsToAdapter (
        movieReviews: List<Review>
    ) {
        movieReviewsAdapter = ReviewAdapter(
            movieReviews,
            this,
            this.requireContext()
        )
        binding.rVMovieReviews.adapter = movieReviewsAdapter
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

    override fun onItemClicked(selectedItem: Int, movieIDSelected: String) {
        viewModel.onSimilarMovieClicked(movieIDSelected)
    }

    override fun onItemClicked(selectedItem: Int, reviewSelected: Review) {
        this.requireActivity().toast("Review by: ${reviewSelected.author.toString()}")
    }
}