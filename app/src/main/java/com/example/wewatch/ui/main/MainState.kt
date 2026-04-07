package com.example.wewatch.ui.main

import com.example.wewatch.data.local.MovieEntity

sealed class MainState {
    object Loading : MainState()
    data class Success(val movies: List<MovieEntity>) : MainState()
    data class Error(val message: String) : MainState()
    object Empty : MainState()
}