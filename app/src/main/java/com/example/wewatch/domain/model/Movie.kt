package com.example.wewatch.domain.model

data class Movie(
    val id: String,
    val title: String,
    val year: String,
    val poster: String?,
    val genre: String?
)