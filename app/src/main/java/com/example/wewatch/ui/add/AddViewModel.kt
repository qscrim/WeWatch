package com.example.wewatch.ui.add

import android.content.Context
import com.example.wewatch.data.local.MovieEntity
import com.example.wewatch.data.repository.MovieRepository
import com.example.wewatch.di.ServiceLocator
import com.example.wewatch.ui.base.MviViewModel

class AddViewModel(context: Context) : MviViewModel<AddIntent, AddState>(AddState.Initial) {

    private val repository: MovieRepository = ServiceLocator.provideRepository(context)

    override fun observeIntents() {
        // Обработка Intent делегирована в handleIntent
    }

    override suspend fun handleIntent(currentState: AddState, intent: AddIntent): AddState {
        return when (intent) {
            is AddIntent.LoadMovieDetails -> loadMovieDetails()
            is AddIntent.SaveMovie -> saveMovie(intent.movie)
            is AddIntent.LoadMovieData -> loadMovieData(intent.imdbID)
        }
    }

    private suspend fun loadMovieDetails(): AddState {
        return AddState.Loading
    }

    private suspend fun loadMovieData(imdbID: String): AddState {
        return try {
            val response = repository.getMovieDetails(imdbID)
            AddState.Success(response)
        } catch (e: Exception) {
            AddState.Error(e.message ?: "Ошибка загрузки")
        }
    }

    private suspend fun saveMovie(movie: MovieEntity): AddState {
        repository.addMovieToLocal(movie)
        return state.replayCache.firstOrNull() ?: AddState.Initial
    }
}