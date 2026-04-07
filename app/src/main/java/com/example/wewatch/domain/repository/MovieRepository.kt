package com.example.wewatch.domain.repository

import com.example.wewatch.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getLocalMovies(): Flow<List<Movie>>
    suspend fun addMovie(movie: Movie)
    suspend fun deleteMovies(movies: List<Movie>)
    suspend fun searchMoviesOnline(query: String, year: String?): List<Movie>
    suspend fun getMovieDetails(imdbID: String): Movie
}