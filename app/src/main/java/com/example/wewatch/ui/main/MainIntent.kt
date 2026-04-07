package com.example.wewatch.ui.main

import com.example.wewatch.data.local.MovieEntity
import com.example.wewatch.ui.base.MviIntent

sealed class MainIntent : MviIntent() {
    object LoadMovies : MainIntent()
    data class DeleteMovies(val movies: List<MovieEntity>) : MainIntent()
}