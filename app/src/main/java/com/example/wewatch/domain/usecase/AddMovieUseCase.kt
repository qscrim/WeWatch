package com.example.wewatch.domain.usecase

import com.example.wewatch.domain.model.Movie
import com.example.wewatch.domain.repository.MovieRepository

class AddMovieUseCase(
    private val repository: MovieRepository
) : UseCase<Unit, AddMovieUseCase.Params>() {

    override suspend fun execute(params: Params) {
        repository.addMovie(params.movie)
    }

    data class Params(val movie: Movie)
}