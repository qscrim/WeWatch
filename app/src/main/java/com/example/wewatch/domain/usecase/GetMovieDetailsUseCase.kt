package com.example.wewatch.domain.usecase

import com.example.wewatch.domain.model.Movie
import com.example.wewatch.domain.repository.MovieRepository

class GetMovieDetailsUseCase(
    private val repository: MovieRepository
) : UseCase<Movie, GetMovieDetailsUseCase.Params>() {

    override suspend fun execute(params: Params): Movie {
        return repository.getMovieDetails(params.imdbID)
    }

    data class Params(val imdbID: String)
}