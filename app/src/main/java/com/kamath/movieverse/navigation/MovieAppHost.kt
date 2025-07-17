package com.kamath.movieverse.navigation

import MovieListScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kamath.movieverse.ui.screens.MovieDetailsScreen
import com.kamath.movieverse.ui.screens.MovieLikedScreen

@Composable
fun MovieAppHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "movies_list"
    ) {
        composable("movies_list") {
            MovieListScreen(navController = navController)
        }
        composable(
            route = "movie_details/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            MovieDetailsScreen(movieId = movieId)
        }
        composable("liked_movies") {
            MovieLikedScreen(navController = navController)
        }
    }
}