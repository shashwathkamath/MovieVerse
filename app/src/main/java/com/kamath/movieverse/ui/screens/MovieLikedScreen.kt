package com.kamath.movieverse.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.kamath.movieverse.ui.screens.components.LikedMovieCard
import com.kamath.movieverse.viewmodels.MovieViewModel
import kotlinx.coroutines.launch

@Composable
fun MovieLikedScreen(navController: NavController) {
    val viewmodel: MovieViewModel = hiltViewModel()
    val likedMovies = viewmodel.likedMovies.collectAsLazyPagingItems()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        viewmodel.clearLikedMovies()
                        likedMovies.refresh()
                    }
                }
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Clear liked movies"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(likedMovies.itemCount) { index ->
                val movie = likedMovies[index]
                if (movie != null) {
                    LikedMovieCard(
                        imageUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                        title = movie.title,
                        onClick = { navController.navigate("movie_details/${movie.id}") }
                    )
                }
            }
        }
    }
}
