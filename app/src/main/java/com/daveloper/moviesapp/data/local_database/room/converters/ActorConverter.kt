package com.daveloper.moviesapp.data.local_database.room.converters

import androidx.room.TypeConverter
import com.daveloper.moviesapp.data.model.entity.Actor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ActorConverter {
    @TypeConverter
    fun stringToActorList(value: String?): List<Actor?>? {
        val listType: Type = object : TypeToken<ArrayList<Actor?>?>() {}.type
        return Gson().fromJson<List<Actor?>>(value, listType)
    }
    @TypeConverter
    fun actorListToString(list: List<Actor?>?): String? {
        return Gson().toJson(list)
    }
}