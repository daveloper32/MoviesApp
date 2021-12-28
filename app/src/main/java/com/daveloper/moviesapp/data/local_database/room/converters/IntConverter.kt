package com.daveloper.moviesapp.data.local_database.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class IntConverter {
    @TypeConverter
    fun stringToIntList(value: String?): List<Int?>? {
        val listType: Type = object : TypeToken<ArrayList<Int?>?>() {}.type
        return Gson().fromJson<List<Int?>>(value, listType)
    }
    @TypeConverter
    fun intListToString(list: List<Int?>?): String? {
        return Gson().toJson(list)
    }
}