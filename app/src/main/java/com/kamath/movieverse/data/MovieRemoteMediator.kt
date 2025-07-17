package com.kamath.movieverse.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kamath.movieverse.data.api.TmdbApi
import com.kamath.movieverse.data.local.AppDatabase
import com.kamath.movieverse.models.db.MovieEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val tmdbApi: TmdbApi,
    private val database: AppDatabase,
    private val apiKey: String
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastPage = state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.pageId
                    if (lastPage == null) return MediatorResult.Success(endOfPaginationReached = true)
                    lastPage + 1
                }
            }

            val response = tmdbApi.getPopularMovies(apiKey, loadKey)
            val movies = response.results.map { movie ->
                MovieEntity(
                    id = movie.id,
                    title = movie.title,
                    overview = movie.overview,
                    posterPath = movie.posterPath,
                    releaseDate = movie.releaseDate,
                    voteAverage = movie.voteAverage,
                    pageId = loadKey
                )
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.movieDao().clearMovies()
                }
                database.movieDao().insertMovies(movies)
            }

            MediatorResult.Success(endOfPaginationReached = response.page >= response.totalPages)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}