package com.example.wewatch.ui.add

import com.example.wewatch.domain.model.Movie

sealed class AddState {
    object Initial : AddState()
    object Loading : AddState()
    data class Success(val movie: Movie) : AddState()
    data class Error(val message: String) : AddState()
}