package com.daveloper.moviesapp.ui.view

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daveloper.moviesapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MoviesApp)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}