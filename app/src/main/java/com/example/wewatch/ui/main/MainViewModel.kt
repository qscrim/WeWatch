package com.example.wewatch.ui.main

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.wewatch.data.local.MovieEntity
import com.example.wewatch.data.repository.MovieMapper
import com.example.wewatch.di.ServiceLocator
import com.example.wewatch.domain.model.Movie
import com.example.wewatch.domain.usecase.AddMovieUseCase
import com.example.wewatch.domain.usecase.DeleteMoviesUseCase
import com.example.wewatch.domain.usecase.GetLocalMoviesUseCase
import com.example.wewatch.ui.base.MviViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(context: Context) : MviViewModel<MainIntent, MainState>(MainState.Loading) {

    private val repository = ServiceLocator.provideRepository(context)
    private val getLocalMoviesUseCase = GetLocalMoviesUseCase(repository)
    private val addMovieUseCase = AddMovieUseCase(repository)
    private val deleteMoviesUseCase = DeleteMoviesUseCase(repository)

    init {
        // Наблюдаем за изменениями в базе данных через UseCase
        viewModelScope.launch {
            getLocalMoviesUseCase.execute(com.example.wewatch.domain.usecase.UseCase.None()).collectLatest { movies ->
                if (movies.isEmpty()) {
                    setState(MainState.Empty)
                } else {
                    // Маппим Domain Movie -> Data MovieEntity для UI
                    val movieEntities = movies.map { movie ->
                        MovieEntity(
                            id = movie.id,
                            title = movie.title,
                            year = movie.year,
                            poster = movie.poster,
                            genre = movie.genre
                        )
                    }
                    setState(MainState.Success(movieEntities))
                }
            }
        }
    }

    override fun observeIntents() {
        // Intent'ы обрабатываются в handleIntent
    }

    override suspend fun handleIntent(currentState: MainState, intent: MainIntent): MainState {
        return when (intent) {
            is MainIntent.LoadMovies -> currentState
            is MainIntent.DeleteMovies -> {
                // Маппим MovieEntity -> Domain Movie для UseCase
                val movies = intent.movies.map { entity ->
                    Movie(
                        id = entity.id,
                        title = entity.title,
                        year = entity.year,
                        poster = entity.poster,
                        genre = entity.genre
                    )
                }
                deleteMoviesUseCase.execute(DeleteMoviesUseCase.Params(movies))
                currentState
            }
        }
    }
}