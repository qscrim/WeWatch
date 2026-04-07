package com.example.wewatch.ui.search

import com.example.wewatch.ui.base.MviIntent

sealed class SearchIntent : MviIntent() {
    data class SearchMovies(val query: String, val year: String? = null) : SearchIntent()
    data class SelectMovie(val movieId: String) : SearchIntent()
}