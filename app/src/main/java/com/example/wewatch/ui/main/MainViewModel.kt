package com.example.wewatch.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.wewatch.data.local.MovieEntity
import com.example.wewatch.data.repository.MovieRepository
import com.example.wewatch.di.ServiceLocator
import com.example.wewatch.ui.base.BaseViewModel

class MainViewModel(context: Context) : BaseViewModel() {

    private val repository: MovieRepository = ServiceLocator.provideRepository(context)

    // LiveData для наблюдения за списком фильмов
    val movies: LiveData<List<MovieEntity>> = repository.getLocalMovies().asLiveData()

    // Метод для добавления фильма
    fun addMovie(movie: MovieEntity) {
        launchWhenStarted {
            repository.addMovieToLocal(movie)
        }
    }

    // Метод для удаления выбранных фильмов
    fun deleteMovies(movies: List<MovieEntity>) {
        launchWhenStarted {
            repository.deleteMoviesFromLocal(movies)
        }
    }
}