package com.kamath.movieverse.di

import android.content.Context
import androidx.room.Room
import com.kamath.movieverse.data.local.AppDatabase
import com.kamath.movieverse.data.local.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_database").build()

    @Provides
    @Singleton
    fun provideMovieDao(database: AppDatabase): MovieDao = database.movieDao()
}