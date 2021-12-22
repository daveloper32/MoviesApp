package com.daveloper.moviesapp.data.local_database.room.converters

import androidx.room.TypeConverter
import com.daveloper.moviesapp.data.model.entity.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class MovieConverter {
    @TypeConverter
    fun stringToMovieList(value: String?): List<Movie?>? {
        val listType: Type = object : TypeToken<ArrayList<Movie?>?>() {}.type
        return Gson().fromJson<List<Movie?>>(value, listType)
    }
    @TypeConverter
    fun novieListToString(list: List<Movie?>?): String? {
        return Gson().toJson(list)
    }
}
