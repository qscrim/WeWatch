package com.example.wewatch.data.repository

import com.example.wewatch.data.local.MovieEntity
import com.example.wewatch.domain.model.Movie

object MovieMapper {
    fun toDomain(entity: MovieEntity): Movie {
        return Movie(
            id = entity.id,
            title = entity.title,
            year = entity.year,
            poster = entity.poster,
            genre = entity.genre
        )
    }

    fun toDomainList(entities: List<MovieEntity>): List<Movie> {
        return entities.map { toDomain(it) }
    }

    fun toData(model: Movie): MovieEntity {
        return MovieEntity(
            id = model.id,
            title = model.title,
            year = model.year,
            poster = model.poster,
            genre = model.genre
        )
    }
}