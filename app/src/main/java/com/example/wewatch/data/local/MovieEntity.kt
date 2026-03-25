package com.example.wewatch.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: String,      // IMDb ID
    val title: String,
    val year: String,
    val poster: String,
    val genre: String = ""
)