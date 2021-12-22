package com.daveloper.moviesapp.ui.view.movie_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.daveloper.moviesapp.R
import com.daveloper.moviesapp.databinding.FragmentMovieDetailsBinding
import com.daveloper.moviesapp.ui.viewmodel.movie_details.MovieDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    // Init Vars
    // Binding
    private lateinit var binding: FragmentMovieDetailsBinding
    // ViewModel
    private  val viewModel: MovieDetailsViewModel by viewModels<MovieDetailsViewModel>()
    // Adapters RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailsBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }
}