package com.daveloper.moviesapp.ui.view.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.daveloper.moviesapp.R
import com.daveloper.moviesapp.databinding.FragmentMoviesBinding
import com.daveloper.moviesapp.ui.viewmodel.movies.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    // Init Vars
    // Binding
    private lateinit var binding: FragmentMoviesBinding
    // ViewModel
    private val viewModel: MoviesViewModel by viewModels<MoviesViewModel>()
    // Adapters RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

}