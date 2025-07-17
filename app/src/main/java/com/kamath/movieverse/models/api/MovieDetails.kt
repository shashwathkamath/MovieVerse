package com.kamath.movieverse.models.api

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    val runtime: Int?,
    val genres: List<Genre>?,
    val budget: Long?,
    val revenue: Long?
)
