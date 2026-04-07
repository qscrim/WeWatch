package com.example.wewatch.ui.add

import android.content.Context
import com.example.wewatch.di.ServiceLocator
import com.example.wewatch.domain.usecase.AddMovieUseCase
import com.example.wewatch.domain.usecase.GetMovieDetailsUseCase
import com.example.wewatch.ui.base.MviViewModel

class AddViewModel(context: Context) : MviViewModel<AddIntent, AddState>(AddState.Initial) {

    private val repository = ServiceLocator.provideRepository(context)
    private val addMovieUseCase = AddMovieUseCase(repository)
    private val getMovieDetailsUseCase = GetMovieDetailsUseCase(repository)

    override fun observeIntents() {
        // Intent'ы обрабатываются в handleIntent
    }

    override suspend fun handleIntent(currentState: AddState, intent: AddIntent): AddState {
        return when (intent) {
            is AddIntent.LoadMovieDetails -> AddState.Loading
            is AddIntent.SaveMovie -> saveMovie(intent.movie)
            is AddIntent.LoadMovieData -> loadMovieData(intent.imdbID)
        }
    }

    private suspend fun loadMovieDetails(imdbID: String): AddState {
        return try {
            val movie = getMovieDetailsUseCase.execute(GetMovieDetailsUseCase.Params(imdbID))
            AddState.Success(movie)
        } catch (e: Exception) {
            AddState.Error(e.message ?: "Ошибка загрузки")
        }
    }

    private suspend fun saveMovie(movie: com.example.wewatch.domain.model.Movie): AddState {
        addMovieUseCase.execute(AddMovieUseCase.Params(movie))
        return AddState.Success(movie)
    }

    private suspend fun loadMovieData(imdbID: String): AddState {
        return loadMovieDetails(imdbID)
    }
}