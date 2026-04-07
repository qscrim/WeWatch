package com.example.wewatch.ui.search

import com.example.wewatch.data.remote.MovieSearchResult

sealed class SearchState {
    object Loading : SearchState()
    data class Success(val movies: List<MovieSearchResult>) : SearchState()
    data class Error(val message: String) : SearchState()
    object Empty : SearchState()
}