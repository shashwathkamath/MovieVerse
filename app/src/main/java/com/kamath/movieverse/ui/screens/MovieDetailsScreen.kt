package com.kamath.movieverse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.kamath.movieverse.viewmodels.MovieViewModel

@Composable
fun MovieDetailsScreen(movieId: Int) {
    val viewmodel: MovieViewModel = hiltViewModel()
    val movieDetails by viewmodel.movieDetails.collectAsState(initial = null)
    val error by viewmodel.error.collectAsState(initial = null)
    var isFav by remember { mutableStateOf(false) }

    LaunchedEffect(movieId) {
        viewmodel.loadMovieDetails(movieId)
        viewmodel.isLiked(movieId).collect { liked ->
            isFav = liked
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFF0))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
       when{
           error != null -> Text("Error in loading")
           movieDetails!=null -> {
               Card(
                   shape = RoundedCornerShape(16.dp),
                   modifier = Modifier
                       .fillMaxWidth()
                       .height(300.dp)
               ) {
                   Box {
                       AsyncImage(
                           model = "https://image.tmdb.org/t/p/w500${movieDetails?.posterPath}",
                           contentDescription = movieDetails?.title,
                           modifier = Modifier
                               .fillMaxWidth()
                               .height(300.dp),
                           contentScale = ContentScale.Crop
                       )
                       IconButton(
                           onClick = {
                               isFav = !isFav
                               viewmodel.toggleLike(movieId, isFav)
                           },
                           modifier = Modifier
                               .align(Alignment.TopEnd)
                               .padding(8.dp)
                               .clip(CircleShape)
                               .size(32.dp)
                       ) {
                           Icon(
                               imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                               contentDescription = "Favorite",
                               tint = if (isFav) Color.Red else Color.Gray,
                           )
                       }
                   }
               }
               Spacer(modifier = Modifier.height(16.dp))
               Text(
                   text = "Overview",
                   fontSize = 20.sp,
                   fontWeight = FontWeight.SemiBold
               )
               Spacer(modifier = Modifier.height(8.dp))
               Text(
                   text = movieDetails?.overview ?: "No overview available",
                   fontSize = 14.sp
               )
               Spacer(modifier = Modifier.height(16.dp))
               Text(
                   text = movieDetails?.title ?: "",
                   fontSize = 24.sp,
                   fontWeight = FontWeight.Bold
               )
               Spacer(modifier = Modifier.height(8.dp))
               Text(
                   text = "Release Date: ${movieDetails?.releaseDate ?: "N/A"}",
                   fontSize = 16.sp
               )
               Spacer(modifier = Modifier.height(4.dp))
               Text(
                   text = "Rating: ${movieDetails?.voteAverage ?: 0.0}/10",
                   fontSize = 16.sp
               )
               Spacer(modifier = Modifier.height(4.dp))
               Text(
                   text = "Runtime: ${movieDetails?.runtime ?: 0} minutes",
                   fontSize = 16.sp
               )

           }
           else -> {
               Text("Loading...")
           }
       }
    }
}