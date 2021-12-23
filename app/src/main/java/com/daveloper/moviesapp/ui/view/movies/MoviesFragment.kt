package com.daveloper.moviesapp.ui.view.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.daveloper.moviesapp.auxiliar.ext_fun.activity_context.getStringResource
import com.daveloper.moviesapp.auxiliar.ext_fun.activity_context.toast
import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.databinding.FragmentMoviesBinding
import com.daveloper.moviesapp.ui.view.movies.adapters.MovieAdapter
import com.daveloper.moviesapp.ui.viewmodel.movies.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment(),
        MovieAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener
{

    // Init Vars
    // Binding
    private lateinit var binding: FragmentMoviesBinding
    // ViewModel
    private val viewModel: MoviesViewModel by viewModels<MoviesViewModel>()
    // Adapters RecyclerView
    // Popular Movies
    private lateinit var popularMoviesAdapter: MovieAdapter
    // User Favorite Movies
    private lateinit var userFavoriteMoviesAdapter: MovieAdapter
    // Now Playing Movies
    private lateinit var nowPlayingMovieAdapter: MovieAdapter
    // Upcoming Movies
    private lateinit var upcomingMoviesAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesBinding.inflate(inflater)
        initView()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initView() {
        // init LiveData Observers
        initLiveData()
        // Layout managers for each recyclerView
        binding.rVPopularMovies.layoutManager =
            LinearLayoutManager(
                this.requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        binding.rVUserFavoriteMovies.layoutManager = LinearLayoutManager(
            this.requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rVNowPlayingMovies.layoutManager = LinearLayoutManager(
            this.requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rVUpcomingMovies.layoutManager = LinearLayoutManager(
            this.requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        // onCreate viewModel
        viewModel.onCreate()
        // Listeners
        binding.refreshMovies.setOnRefreshListener(this)
    }
    // Popular Movies
    // User Favorite Movies
    // Now Playing Movies
    // Upcoming Movies

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
        viewModel.goToMovieInfoFragment.observe(
            this,
            Observer {
                // Go to the Movie Details Fragment
                if (it != null) {
                    if (it>0) {
                        findNavController()
                            .navigate(
                                MoviesFragmentDirections
                                    .actionMoviesFragmentToMovieDetailsFragment(it)
                            )
                        viewModel.navigationCompleted()
                    }
                }
            }
        )
        //// Refresh
        viewModel.refreshVisibility.observe(
            this,
            Observer {
                binding.refreshMovies.isRefreshing = it
            }
        )
        //// RecyclerView Data
        // Popular Movies
        viewModel.popularMoviesData.observe(
            this,
            Observer {
                sendPopularMoviesToAdapter(it)
            }
        )
        // User Favorite Movies
        viewModel.userFavoriteMoviesData.observe(
            this,
            Observer {
                sendUserFavoriteMoviesToAdapter(it)
            }
        )
        // Now Playing Movies
        viewModel.nowPlayingMoviesData.observe(
            this,
            Observer {
                sendNowPlayingMoviesAdapter(it)
            }
        )
        // Upcoming Movies
        viewModel.upcomingMoviesData.observe(
            this,
            Observer {
                sendUpcomingMovies(it)
            }
        )
        //// Progressbar
        // Popular Movies
        viewModel.progressPopularMoviesVisibility.observe(
            this,
            Observer {
                //TODO
            }
        )
        // User Favorite Movies
        viewModel.progressUserFavoriteMoviesVisibility.observe(
            this,
            Observer {
                //TODO
            }
        )
        // Now Playing Movies
        viewModel.progressNowPlayingMoviesVisibility.observe(
            this,
            Observer {
                //TODO
            }
        )
        // Upcoming Movies
        viewModel.progressUpcomingMoviesVisibility.observe(
            this,
            Observer {
                //TODO
            }
        )
    }

    // Popular Movies
    private fun sendPopularMoviesToAdapter (
        moviesList: List<Movie>
    ) {
        popularMoviesAdapter = MovieAdapter(
            moviesList,
            this
        )
        binding.rVPopularMovies.adapter = popularMoviesAdapter
    }
    // User Favorite Movies
    private fun sendUserFavoriteMoviesToAdapter (
        moviesList: List<Movie>
    ) {
        userFavoriteMoviesAdapter = MovieAdapter(
            moviesList,
            this
        )
        binding.rVUserFavoriteMovies.adapter = userFavoriteMoviesAdapter
    }
    // Now Playing Movies
    private fun sendNowPlayingMoviesAdapter (
        moviesList: List<Movie>
    ) {
        nowPlayingMovieAdapter = MovieAdapter(
            moviesList,
            this
        )
        binding.rVNowPlayingMovies.adapter = nowPlayingMovieAdapter
    }
    // Upcoming Movies
    private fun sendUpcomingMovies (
        moviesList: List<Movie>
    ) {
        upcomingMoviesAdapter = MovieAdapter(
            moviesList,
            this
        )
        binding.rVUpcomingMovies.adapter = upcomingMoviesAdapter
    }


    override fun onItemClicked(selectedItem: Int, movieIDSelected: String) {
        viewModel.onMovieClicked(movieIDSelected)
    }

    override fun onRefresh() {
        viewModel.onRefresh()
    }

}