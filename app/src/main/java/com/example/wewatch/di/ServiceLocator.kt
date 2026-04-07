package com.example.wewatch.di

import android.content.Context
import com.example.wewatch.data.local.AppDatabase
import com.example.wewatch.data.local.MovieDao
import com.example.wewatch.data.remote.OmdbApi
import com.example.wewatch.data.repository.MovieRepositoryImpl
import com.example.wewatch.domain.repository.MovieRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceLocator {

    private var database: AppDatabase? = null
    private var repository: MovieRepository? = null

    private const val BASE_URL = "http://www.omdbapi.com/"

    fun provideDatabase(context: Context): AppDatabase {
        return database ?: synchronized(this) {
            database ?: AppDatabase.getDatabase(context).also { database = it }
        }
    }

    fun provideMovieDao(context: Context): MovieDao {
        return provideDatabase(context).movieDao()
    }

    fun provideApiService(): OmdbApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OmdbApi::class.java)
    }

    fun provideRepository(context: Context): MovieRepository {
        return repository ?: synchronized(this) {
            repository ?: MovieRepositoryImpl(
                movieDao = provideMovieDao(context),
                apiService = provideApiService()
            ).also { repository = it }
        }
    }

    fun clear() {
        database = null
        repository = null
    }
}