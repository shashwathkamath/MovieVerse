package com.kamath.movieverse.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kamath.movieverse.models.db.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
}