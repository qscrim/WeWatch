package com.example.wewatch.ui.add

import com.example.wewatch.data.remote.MovieDetailsResponse

sealed class AddState {
    object Initial : AddState()
    object Loading : AddState()
    data class Success(val movie: MovieDetailsResponse) : AddState()
    data class Error(val message: String) : AddState()
}