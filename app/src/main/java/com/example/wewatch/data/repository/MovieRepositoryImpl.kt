package com.example.wewatch.data.repository

import com.example.wewatch.data.local.MovieDao
import com.example.wewatch.data.local.MovieEntity
import com.example.wewatch.data.remote.OmdbApi
import com.example.wewatch.domain.model.Movie
import com.example.wewatch.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val movieDao: MovieDao,
    private val apiService: OmdbApi
) : MovieRepository {

    companion object {
        private const val API_KEY = "6f8bdc09" // Замени на свой API ключ!
    }

    override fun getLocalMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies().map { entities ->
            entities.map { entity ->
                Movie(
                    id = entity.id,
                    title = entity.title,
                    year = entity.year,
                    poster = entity.poster,
                    genre = entity.genre
                )
            }
        }
    }

    override suspend fun addMovie(movie: Movie) {
        val entity = MovieEntity(
            id = movie.id,
            title = movie.title,
            year = movie.year,
            poster = movie.poster,
            genre = movie.genre
        )
        movieDao.insertMovie(entity)
    }

    override suspend fun deleteMovies(movies: List<Movie>) {
        val entities = movies.map { movie ->
            MovieEntity(
                id = movie.id,
                title = movie.title,
                year = movie.year,
                poster = movie.poster,
                genre = movie.genre
            )
        }
        movieDao.deleteMovies(entities)
    }

    override suspend fun searchMoviesOnline(query: String, year: String?): List<Movie> {
        val response = if (year != null && year.isNotEmpty()) {
            apiService.searchMovies(API_KEY, query, year)
        } else {
            apiService.searchMovies(API_KEY, query, "")
        }

        return response.Search?.map { searchResult ->
            Movie(
                id = searchResult.imdbID,
                title = searchResult.Title,
                year = searchResult.Year,
                poster = searchResult.Poster,
                genre = null
            )
        } ?: emptyList()
    }

    override suspend fun getMovieDetails(imdbID: String): Movie {
        val response = apiService.getMovieDetails(
            apiKey = API_KEY,
            imdbID = imdbID
        )
        return Movie(
            id = response.imdbID,
            title = response.Title,
            year = response.Year,
            poster = response.Poster,
            genre = response.Genre
        )
    }
}