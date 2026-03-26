package com.example.wewatch.ui.search

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.data.remote.MovieSearchResult
import com.example.wewatch.data.repository.MovieRepository
import com.example.wewatch.di.ServiceLocator
import kotlinx.coroutines.launch

class SearchViewModel(context: Context) : ViewModel() {

    private val repository: MovieRepository = ServiceLocator.provideRepository(context)

    sealed class UiState {
        object Loading : UiState()
        data class Success(val movies: List<MovieSearchResult>) : UiState()
        data class Error(val message: String) : UiState()
        object Empty : UiState()
    }

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    fun searchMovies(query: String, year: String? = null) {
        if (query.isBlank()) {
            _uiState.value = UiState.Empty
            return
        }

        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val response = repository.searchMoviesOnline(query, year)

                // Извлекаем список фильмов из SearchResponse
                val movies = response.Search?.toList() ?: emptyList()

                if (movies.isEmpty()) {
                    _uiState.value = UiState.Empty
                } else {
                    _uiState.value = UiState.Success(movies)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Ошибка поиска")
            }
        }
    }
}