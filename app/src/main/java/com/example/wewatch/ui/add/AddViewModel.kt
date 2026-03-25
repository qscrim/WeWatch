package com.example.wewatch.ui.add

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wewatch.data.local.MovieEntity
import com.example.wewatch.data.remote.MovieDetailsResponse
import com.example.wewatch.data.repository.MovieRepository
import com.example.wewatch.di.ServiceLocator
import com.example.wewatch.ui.base.BaseViewModel

class AddViewModel(context: Context) : BaseViewModel() {

    private val repository: MovieRepository = ServiceLocator.provideRepository(context)

    // Данные выбранного фильма
    private val _movieData = MutableLiveData<MovieDetailsResponse?>()
    val movieData: LiveData<MovieDetailsResponse?> = _movieData

    // Состояние UI
    sealed class UiState {
        object Initial : UiState()
        object Loading : UiState()
        data class Success(val movie: MovieDetailsResponse) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    // Метод для получения деталей фильма
    fun getMovieDetails(imdbID: String) {
        _uiState.value = UiState.Loading

        launchWhenStarted {
            try {
                val details = repository.getMovieDetails(imdbID)
                _movieData.value = details
                _uiState.value = UiState.Success(details)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Ошибка получения данных")
            }
        }
    }

    // Метод для добавления фильма в БД
    fun addMovieToDatabase(movie: MovieEntity) {
        launchWhenStarted {
            repository.addMovieToLocal(movie)
        }
    }

    // Установка данных из SearchActivity
    fun setMovieData(title: String, year: String, poster: String, imdbID: String, genre: String) {
        _movieData.value = MovieDetailsResponse(
            Title = title,
            Year = year,
            Poster = poster,
            Genre = genre,
            imdbID = imdbID
        )
    }
}