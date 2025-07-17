package com.kamath.movieverse.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kamath.movieverse.models.db.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies:List<MovieEntity>)

    @Query("SELECT * FROM movies ORDER BY pageId ASC,id ASC")
    fun getMovies(): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movies")
    suspend fun clearMovies()

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity?

    @Transaction
    suspend fun refreshMovies(movies:List<MovieEntity>){
        clearMovies()
        insertMovies(movies)
    }

    @Query("UPDATE movies SET isLiked = :isLiked WHERE id = :movieId")
    suspend fun updateLikeStatus(movieId: Int, isLiked: Boolean)

    @Query("SELECT * FROM movies WHERE isLiked = 1 ORDER BY title ASC")
    fun getLikedMovies(): PagingSource<Int, MovieEntity>

    @Query("SELECT isLiked FROM movies WHERE id = :movieId")
    fun isMovieLiked(movieId: Int): Flow<Boolean>

    @Query("UPDATE movies SET isLiked = 0")
    suspend fun clearLikes()
}