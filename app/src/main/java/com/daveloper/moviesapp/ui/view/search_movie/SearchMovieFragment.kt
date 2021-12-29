package com.daveloper.moviesapp.ui.view.search_movie

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
import com.daveloper.moviesapp.auxiliar.ext_fun.activity_context.toast
import com.daveloper.moviesapp.data.model.entity.Movie
import com.daveloper.moviesapp.databinding.FragmentSearchMovieBinding
import com.daveloper.moviesapp.ui.view.search_movie.adapters.MovieVerticalAdapter
import com.daveloper.moviesapp.ui.viewmodel.search_movie.SearchMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMovieFragment : Fragment(),
        MovieVerticalAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        android.widget.SearchView.OnQueryTextListener
{

    // Init Vars
    // Binding
    private lateinit var binding: FragmentSearchMovieBinding
    // ViewModel
    private val viewModel: SearchMovieViewModel by viewModels<SearchMovieViewModel>()
    // Adapters RecyclerView
    private lateinit var searchMoviesAdapter: MovieVerticalAdapter
    // SearchKey
    private lateinit var searchTextKey: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchMovieBinding.inflate(inflater)
        initView()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initView() {
        // init LiveData Observers
        initLiveData()
        // Layout managers for each recyclerView
        binding.rVSearchMovies.layoutManager =
            LinearLayoutManager(
                this.requireContext()
            )
        searchTextKey = ""
        // Listeners
        // Search View
        binding.sVMovies.setOnQueryTextListener(this)
        // Refresh
        binding.refreshMovies.setOnRefreshListener(this)
    }

    private fun initLiveData() {
        // Progress bar
        viewModel.setProgressVisibility.observe(
            this,
            Observer {
                if (it) {
                    binding.pgsBSearchMovie.visibility = View.VISIBLE
                } else {
                    binding.pgsBSearchMovie.visibility = View.GONE
                }
            }
        )
        // Show info msg
        viewModel.showInfoMessage.observe(
            this,
            Observer {
                this.requireActivity().toast(it)
            }
        )
        // Navigation
        viewModel.goToMovieInfoFragment.observe(
            this,
            Observer {
                if (it != null) {
                    if (it>0) {
                        findNavController()
                            .navigate(
                                SearchMovieFragmentDirections
                                    .actionSearchMovieFragmentToMovieDetailsFragment(it)
                            )
                        viewModel.navigationCompleted()
                    }
                }

            }
        )
        // Refreshing
        viewModel.refreshVisibility.observe(
            this,
            Observer {
                binding.refreshMovies.isRefreshing = it
            }
        )
        // RecyclerView Data
        // Movies found
        viewModel.moviesFoundData.observe(
            this,
            Observer {
                sendFoundMoviesToAdapter(it)
            }
        )
        // setText
        viewModel.setRVText.observe(
            this,
            Observer {
                binding.tVNoMoviesFound.text = it
            }
        )
        // visibility
        viewModel.setRVTextVisibility.observe(
            this,
            Observer {
                if (it) {
                    binding.tVNoMoviesFound.visibility = View.VISIBLE
                } else {
                    binding.tVNoMoviesFound.visibility = View.GONE
                }
            }
        )
    }

    private fun sendFoundMoviesToAdapter (
        moviesFound: List<Movie>
    ) {
        searchMoviesAdapter = MovieVerticalAdapter(
            moviesFound,
            this
        )
        binding.rVSearchMovies.adapter = searchMoviesAdapter
    }

    override fun onItemClicked(selectedItem: Int, movieIDSelected: String) {
        viewModel.onMovieClicked(movieIDSelected)
    }

    override fun onRefresh() {
        viewModel.onRefresh(searchTextKey)
    }

    override fun onQueryTextSubmit(searchText: String?): Boolean {
        if (searchText != null) {
            searchTextKey = searchText
            viewModel.searchMovies(searchText)
        } else {
            searchTextKey = ""
            viewModel.searchMovies("")
        }
        return false
    }

    override fun onQueryTextChange(searchText: String?): Boolean {
        if (searchText != null) {
            searchTextKey = searchText
            viewModel.searchMovies(searchText)
        } else {
            searchTextKey = ""
            viewModel.searchMovies("")
        }
        return false
    }


}