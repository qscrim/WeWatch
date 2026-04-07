package com.example.wewatch.domain.usecase

import com.example.wewatch.domain.model.Movie
import com.example.wewatch.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetLocalMoviesUseCase(
    private val repository: MovieRepository
) : UseCase<Flow<List<Movie>>, UseCase.None>() {

    override suspend fun execute(params: UseCase.None): Flow<List<Movie>> {
        return repository.getLocalMovies()
    }
}