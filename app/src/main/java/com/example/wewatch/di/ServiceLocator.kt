package com.example.wewatch.di

import android.content.Context
import com.example.wewatch.data.local.AppDatabase
import com.example.wewatch.data.repository.MovieRepository

object ServiceLocator {
    @Volatile
    private var database: AppDatabase? = null
    @Volatile
    private var repository: MovieRepository? = null

    fun provideDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            AppDatabase.getDatabase(context).also { database = it }
        }
    }

    fun provideRepository(context: Context): MovieRepository {
        return repository ?: synchronized(this) {
            MovieRepository(
                database = provideDatabase(context)
            ).also { repository = it }
        }
    }

    // Для тестов или сброса
    fun clear() {
        database = null
        repository = null
    }
}