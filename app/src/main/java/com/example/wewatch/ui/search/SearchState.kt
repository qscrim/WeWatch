package com.example.wewatch.ui.search

import com.example.wewatch.domain.model.Movie

sealed class SearchState {
    object Loading : SearchState()
    data class Success(val movies: List<Movie>) : SearchState()
    data class Error(val message: String) : SearchState()
    object Empty : SearchState()
}