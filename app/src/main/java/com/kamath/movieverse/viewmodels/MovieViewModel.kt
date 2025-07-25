package com.kamath.movieverse.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kamath.movieverse.models.api.Movie
import com.kamath.movieverse.models.api.MovieDetails
import com.kamath.movieverse.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _timeWindow = MutableStateFlow("day")
    val timeWindow = _timeWindow.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val movies: Flow<PagingData<Movie>> = _timeWindow.flatMapLatest { window ->
        movieRepository.getMovies(window).cachedIn(viewModelScope)
    }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 1
    )

    private val _refreshLiked = MutableStateFlow(0)

    private val _movieDetails = MutableStateFlow<MovieDetails?>(null)
    val movieDetails = _movieDetails.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                val details = movieRepository.getMovieDetails(movieId)
                _movieDetails.value = details
            } catch (e: Exception) {
                _error.value = "Failed to load movies: ${e.message}"
            }
        }
    }

    fun setWindow(window: String) {
        _timeWindow.value = window
    }

    fun toggleLike(movieId: Int, isLiked: Boolean) {
        viewModelScope.launch {
            movieRepository.toggleLike(movieId, isLiked)
            _refreshLiked.value++  // Only refresh liked list
        }
    }

    fun isLiked(movieId: Int): Flow<Boolean> = movieRepository.isMovieLiked(movieId)

}