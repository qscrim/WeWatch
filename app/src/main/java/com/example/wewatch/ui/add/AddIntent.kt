package com.example.wewatch.ui.add

import com.example.wewatch.domain.model.Movie
import com.example.wewatch.ui.base.MviIntent

sealed class AddIntent : MviIntent() {
    object LoadMovieDetails : AddIntent()
    data class SaveMovie(val movie: Movie) : AddIntent()
    data class LoadMovieData(val imdbID: String) : AddIntent()
}