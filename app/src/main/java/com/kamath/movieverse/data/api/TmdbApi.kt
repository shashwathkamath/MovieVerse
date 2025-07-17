package com.kamath.movieverse.data.api

import com.kamath.movieverse.models.api.MovieDetails
import com.kamath.movieverse.models.api.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key")apiKey:String,
        @Query("page")page:Int = 1
    ):MovieResponse

    @GET("3/movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId:Int,
        @Query("api_key")apiKey:String
    ):MovieDetails

    @GET("3/trending/movie/{time_window}")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow:String,
        @Query("api_key")apiKey:String,
        @Query("page") page: Int = 1,
    ): MovieResponse
}