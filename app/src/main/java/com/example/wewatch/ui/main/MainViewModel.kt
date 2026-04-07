package com.example.wewatch.ui.main

import android.content.Context
import com.example.wewatch.data.local.MovieEntity
import com.example.wewatch.data.repository.MovieRepository
import com.example.wewatch.di.ServiceLocator
import com.example.wewatch.ui.base.MviViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainViewModel(context: Context) : MviViewModel<MainIntent, MainState>(MainState.Loading) {

    private val repository: MovieRepository = ServiceLocator.provideRepository(context)

    override fun observeIntents() {
        // Обработка Intent из UI
    }

    override suspend fun handleIntent(currentState: MainState, intent: MainIntent): MainState {
        return when (intent) {
            is MainIntent.LoadMovies -> loadMovies()
            is MainIntent.DeleteMovies -> deleteMovies(intent.movies)
        }
    }

    private suspend fun loadMovies(): MainState {
        return try {
            val movies = repository.getLocalMovies().firstOrNull() ?: emptyList()
            if (movies.isEmpty()) {
                MainState.Empty
            } else {
                MainState.Success(movies)
            }
        } catch (e: Exception) {
            MainState.Error(e.message ?: "Ошибка загрузки")
        }
    }

    private suspend fun deleteMovies(movies: List<MovieEntity>): MainState {
        repository.deleteMoviesFromLocal(movies)
        return loadMovies()
    }
}