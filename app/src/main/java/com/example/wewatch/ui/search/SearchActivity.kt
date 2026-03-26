package com.example.wewatch.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wewatch.data.remote.MovieSearchResult
import com.example.wewatch.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val query = intent.getStringExtra("QUERY") ?: ""
        val year = intent.getStringExtra("YEAR")

        val factory = SearchViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]

        setupRecyclerView()
        observeViewModel()

        viewModel.searchMovies(query, year)
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter { movie ->
            returnResult(movie)
        }
        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        binding.rvSearch.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is SearchViewModel.UiState.Loading -> {
                    showLoading(true)
                }
                is SearchViewModel.UiState.Success -> {
                    showLoading(false)
                    adapter.submitList(state.movies)
                }
                is SearchViewModel.UiState.Error -> {
                    showLoading(false)
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
                is SearchViewModel.UiState.Empty -> {
                    showLoading(false)
                    showEmptyState(true)
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.pbLoading.visibility = if (show) View.VISIBLE else View.GONE
        binding.rvSearch.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showEmptyState(show: Boolean) {
        binding.tvEmptyState.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun returnResult(movie: MovieSearchResult) {
        val resultIntent = Intent().apply {
            putExtra("RESULT_TITLE", movie.Title)
            putExtra("RESULT_YEAR", movie.Year)
            putExtra("RESULT_POSTER", movie.Poster)
            putExtra("RESULT_ID", movie.imdbID)
            putExtra("RESULT_TYPE", movie.Type)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}