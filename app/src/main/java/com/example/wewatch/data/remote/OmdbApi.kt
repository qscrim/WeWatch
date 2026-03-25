package com.example.wewatch.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

data class SearchResponse(
    val Search: List<MovieSearchResult>?,
    val Response: String,
    val Error: String?
)

data class MovieSearchResult(
    val Title: String,
    val Year: String,
    val imdbID: String,
    val Type: String,
    val Poster: String
)

data class MovieDetailsResponse(
    val Title: String,
    val Year: String,
    val Poster: String,
    val Genre: String,
    val imdbID: String
)

interface OmdbApi {
    @GET("/")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") title: String,
        @Query("y") year: String? = null
    ): SearchResponse

    @GET("/")
    suspend fun getMovieDetails(
        @Query("apikey") apiKey: String,
        @Query("i") imdbID: String
    ): MovieDetailsResponse
}