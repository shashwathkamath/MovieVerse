package com.kamath.movieverse.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kamath.movieverse.models.api.Movie
import com.kamath.movieverse.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikedMoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModel(){

    private val _refreshLiked = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val likedMovies: Flow<PagingData<Movie>> = _refreshLiked.flatMapLatest {
        movieRepository.getLikedMovies().cachedIn(viewModelScope)
    }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        replay = 1
    )

    fun deleteMovie(movieId:Int){
        viewModelScope.launch {
            movieRepository.toggleLike(movieId,false)
            _refreshLiked.value++
        }
    }

    fun clearLikedMovies(){
        viewModelScope.launch {
            movieRepository.clearLikedMovies()
            _refreshLiked.value++
        }
    }
}