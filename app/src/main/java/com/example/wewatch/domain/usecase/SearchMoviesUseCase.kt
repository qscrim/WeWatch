package com.example.wewatch.domain.usecase

import com.example.wewatch.domain.model.Movie
import com.example.wewatch.domain.repository.MovieRepository

class SearchMoviesUseCase(
    private val repository: MovieRepository
) : UseCase<List<Movie>, SearchMoviesUseCase.Params>() {

    override suspend fun execute(params: Params): List<Movie> {
        return repository.searchMoviesOnline(params.query, params.year)
    }

    data class Params(
        val query: String,
        val year: String? = null
    )
}