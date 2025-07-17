package com.kamath.movieverse.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kamath.movieverse.BuildConfig
import com.kamath.movieverse.data.MovieRemoteMediator
import com.kamath.movieverse.data.api.TmdbApi
import com.kamath.movieverse.data.local.AppDatabase
import com.kamath.movieverse.models.api.Movie
import com.kamath.movieverse.models.api.MovieDetails
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class MovieRepository @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val database: AppDatabase
){
    private val API_KEY = BuildConfig.TMDB_API_KEY


    @OptIn(ExperimentalPagingApi::class)
    val movies: Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        remoteMediator = MovieRemoteMediator(tmdbApi,database,API_KEY),
        pagingSourceFactory = {database.movieDao().getMovies()}
    ).flow
        .map {pagingData ->
            pagingData.map { entity->
                Movie(
                    id = entity.id,
                    title = entity.title,
                    overview = entity.overview,
                    posterPath = entity.posterPath,
                    releaseDate = entity.releaseDate,
                    voteAverage = entity.voteAverage
                )
            }
        }

    suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return tmdbApi.getMovieDetails(movieId,API_KEY)
    }

    @OptIn(ExperimentalPagingApi::class)
    val likedMovies: Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { database.movieDao().getLikedMovies() }
    ).flow
        .map { pagingData ->
            pagingData.map { entity ->
                Movie(
                    id = entity.id,
                    title = entity.title,
                    overview = entity.overview,
                    posterPath = entity.posterPath,
                    releaseDate = entity.releaseDate,
                    voteAverage = entity.voteAverage
                )
            }
        }

    suspend fun toggleLike(movieId: Int, isLiked: Boolean) {
        database.movieDao().updateLikeStatus(movieId, isLiked)
    }

    suspend fun isMovieLiked(movieId:Int):Boolean?{
        return database.movieDao().isMovieLiked(movieId)
    }

    suspend fun clearLikedMovies(){
        database.movieDao().clearLikes()
    }
}