package com.daveloper.moviesapp.ui.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.daveloper.moviesapp.R
import com.daveloper.moviesapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{
    // Init Vars
    // Binding
    private lateinit var binding: ActivityMainBinding
    //
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        //After Splash style, setting the Theme to Theme_MoviesApp
        setTheme(R.style.Theme_MoviesApp)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        // Reference to the nav controller
        navController = findNavController(R.id.nav_host_fragment_activity_main)


        binding.bottomNav.setupWithNavController(navController)
        // Visibility of the bottom navigation depending on the fragment displayed with navigation component
        setBottomNavigationVisibility()
    }

    private fun setBottomNavigationVisibility() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                // For the Movie Details Fragment we dont need the Bottom Navigation
                R.id.movieDetailsFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
                // For any other Fragment we need the Bottom Navigation
                else -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

}