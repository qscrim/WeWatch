package com.example.wewatch.domain.usecase

import com.example.wewatch.domain.model.Movie
import com.example.wewatch.domain.repository.MovieRepository

class DeleteMoviesUseCase(
    private val repository: MovieRepository
) : UseCase<Unit, DeleteMoviesUseCase.Params>() {

    override suspend fun execute(params: Params) {
        repository.deleteMovies(params.movies)
    }

    data class Params(val movies: List<Movie>)
}