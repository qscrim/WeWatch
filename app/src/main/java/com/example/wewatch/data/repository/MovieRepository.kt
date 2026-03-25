package com.example.wewatch.data.repository

import com.example.wewatch.data.local.AppDatabase
import com.example.wewatch.data.local.MovieEntity
import com.example.wewatch.data.remote.NetworkModule
import kotlinx.coroutines.flow.Flow

class MovieRepository(
    private val database: AppDatabase
) {
    // Локальные данные (Room)
    fun getLocalMovies(): Flow<List<MovieEntity>> {
        return database.movieDao().getAllMovies()
    }

    suspend fun addMovieToLocal(movie: MovieEntity) {
        database.movieDao().insertMovie(movie)
    }

    suspend fun deleteMoviesFromLocal(movies: List<MovieEntity>) {
        database.movieDao().deleteMovies(movies)
    }

    // Удалённые данные (OMDb API) - подготовим методы на будущее
    suspend fun searchMoviesOnline(query: String, year: String? = null) =
        NetworkModule.api.searchMovies(
            NetworkModule.getApiKey(),
            query,
            year
        )

    suspend fun getMovieDetails(imdbID: String) =
        NetworkModule.api.getMovieDetails(
            NetworkModule.getApiKey(),
            imdbID
        )
}