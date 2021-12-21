package com.daveloper.moviesapp.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Movie (
    @PrimaryKey @SerializedName("id") val id: Int,
    @ColumnInfo(name= "movie_name") @SerializedName("title") var name: String? = "",
    @ColumnInfo(name= "movie_original_name") @SerializedName("original_title") var originalName: String? = "",
    @ColumnInfo(name= "movie_overview") @SerializedName("overview") var overview: String? = "",
    @ColumnInfo(name= "movie_release_date") @SerializedName("release_date") var releaseDate: String? = "",
    @ColumnInfo(name= "movie_poster_img_path") @SerializedName("poster_path") var posterImg: String? = "",
    @ColumnInfo(name= "movie_backdrop_poster_img_path") @SerializedName("backdrop_path") var backdropPosterImg: String? = "",
    @ColumnInfo(name= "movie_rating") @SerializedName("vote_average") var rating: String? = "", // 0/10
    @ColumnInfo(name= "movie_adult_classification") @SerializedName("adult") var adultClassification: Boolean? = false, // +18?
    ///
    @ColumnInfo(name= "movie_genres") @SerializedName("genres") var genres: List<Genre>? = emptyList(), // List of Genres
    @ColumnInfo(name= "movie_webpage") @SerializedName("homepage") var webPage: String? = "",
    @ColumnInfo(name= "movie_tagline") @SerializedName("tagline") var tagline: String? = "", // (lema) of the movie
    @ColumnInfo(name= "movie_spoken_languages") @SerializedName("spoken_languages") var spokenLanguages: List<Language>? = emptyList(), // List of Spoken Languages
    @ColumnInfo(name= "movie_production_companies") @SerializedName("production_companies") var productionCompanies: List<ProductionCompany>? = emptyList(), // List of Production Companies

    @ColumnInfo(name= "movie_videos") var videos: List<Video>? = emptyList(), // List of Videos founded
    @ColumnInfo(name= "movie_cast") var cast: List<Actor>? = emptyList(), // List of Cast Actors founded
)
