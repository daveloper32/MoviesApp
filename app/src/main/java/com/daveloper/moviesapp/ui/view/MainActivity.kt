package com.daveloper.moviesapp.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.daveloper.moviesapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //After Splash style, setting the Theme to Theme_MoviesApp
        setTheme(R.style.Theme_MoviesApp)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}