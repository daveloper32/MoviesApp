package com.daveloper.moviesapp.auxiliar.ext_fun.activity_context

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

fun Activity.goToXActivity(activity: Class<out AppCompatActivity?>, finishActivity: Boolean = true){
    // Create an Intent object
    val i: Intent = Intent(this, activity)
    // Starting the Activity that we want to go to
    startActivity(i)
    if (finishActivity) {
        this.finish()
    }
}