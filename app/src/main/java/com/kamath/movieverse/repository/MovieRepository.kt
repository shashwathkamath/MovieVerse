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
import com.kamath.movieverse.models.db.MovieEntity
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
    fun getMovies(timeWindow: String): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        remoteMediator = MovieRemoteMediator(tmdbApi, database, API_KEY, timeWindow),
        pagingSourceFactory = { database.movieDao().getMovies() }
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

    suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return tmdbApi.getMovieDetails(movieId,API_KEY)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getLikedMovies(): Flow<PagingData<Movie>> = Pager(
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

    fun isMovieLiked(movieId:Int):Flow<Boolean> = database.movieDao().isMovieLiked(movieId)

    suspend fun clearLikedMovies(){
        database.movieDao().clearLikes()
    }

    suspend fun insertMovieIfNotExists(entity: MovieEntity) {
        if (database.movieDao().getMovieById(entity.id) == null) {  // Add @Query("SELECT * FROM movies WHERE id = :id") suspend fun getMovieById(id: Int): MovieEntity? to DAO
            database.movieDao().insertMovies(listOf(entity))
        }
    }
}