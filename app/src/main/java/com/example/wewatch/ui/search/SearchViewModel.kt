package com.example.wewatch.ui.search

import android.content.Context
import com.example.wewatch.data.remote.MovieSearchResult
import com.example.wewatch.data.repository.MovieRepository
import com.example.wewatch.di.ServiceLocator
import com.example.wewatch.ui.base.MviViewModel
import kotlinx.coroutines.flow.first

class SearchViewModel(context: Context) : MviViewModel<SearchIntent, SearchState>(SearchState.Empty) {

    private val repository: MovieRepository = ServiceLocator.provideRepository(context)

    override fun observeIntents() {
        // Обработка Intent делегирована в handleIntent
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
                val response = repository.searchMoviesOnline(query, year)
                val movies = response.Search?.toList() ?: emptyList()

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