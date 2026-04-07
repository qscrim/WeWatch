package com.example.wewatch.ui.search

import android.content.Context
import com.example.wewatch.data.repository.MovieMapper
import com.example.wewatch.di.ServiceLocator
import com.example.wewatch.domain.usecase.GetMovieDetailsUseCase
import com.example.wewatch.domain.usecase.SearchMoviesUseCase
import com.example.wewatch.ui.base.MviViewModel

class SearchViewModel(context: Context) : MviViewModel<SearchIntent, SearchState>(SearchState.Empty) {

    private val repository = ServiceLocator.provideRepository(context)
    private val searchMoviesUseCase = SearchMoviesUseCase(repository)
    private val getMovieDetailsUseCase = GetMovieDetailsUseCase(repository)

    override fun observeIntents() {
        // Intent'ы обрабатываются в handleIntent
    }

    override suspend fun handleIntent(currentState: SearchState, intent: SearchIntent): SearchState {
        return when (intent) {
            is SearchIntent.SearchMovies -> searchMovies(intent.query, intent.year)
            is SearchIntent.SelectMovie -> currentState
        }
    }

    private suspend fun searchMovies(query: String, year: String?): SearchState {
        return try {
            if (query.isBlank()) {
                SearchState.Empty
            } else {
                val movies = searchMoviesUseCase.execute(SearchMoviesUseCase.Params(query, year))

                if (movies.isEmpty()) {
                    SearchState.Empty
                } else {
                    SearchState.Success(movies)
                }
            }
        } catch (e: Exception) {
            SearchState.Error(e.message ?: "Ошибка поиска")
        }
    }
}