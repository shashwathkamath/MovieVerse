import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kamath.movieverse.viewmodels.MovieViewModel
import com.kamath.movieverse.ui.screens.components.MovieCard
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(navController: NavController) {
    val viewModel: MovieViewModel = hiltViewModel()
    val movies = viewModel.movies.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MovieVerse", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(
                        onClick = {navController.navigate("liked_movies")}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Liked Movies"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            movies.loadState.refresh is LoadState.Loading -> Text("Loading movies...")
            movies.loadState.refresh is LoadState.Error -> Text("Error: ${(movies.loadState.refresh as LoadState.Error).error.message}")
            movies.itemCount == 0 -> Text("No movies found")
            else -> {
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(movies.itemCount) { index ->
                        val movie = movies[index]
                        if (movie != null) {
                            MovieCard(
                                movie,
                                viewModel = viewModel,
                                onClick = { movieId ->
                                    navController.navigate("movie_details/$movieId")
                                }
                            )
                        }
                    }
                    if (movies.loadState.append is LoadState.Loading) {
                        item { Text("Loading more...") }
                    }
                }
            }
        }
    }

}