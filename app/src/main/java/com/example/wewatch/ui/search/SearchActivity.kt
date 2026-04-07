package com.example.wewatch.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wewatch.data.remote.MovieSearchResult
import com.example.wewatch.databinding.ActivitySearchBinding
import com.example.wewatch.ui.base.MviView

class SearchActivity : AppCompatActivity(), MviView<SearchState> {

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
        observeState()

        // Отправляем Intent для поиска
        viewModel.processIntent(SearchIntent.SearchMovies(query, year))
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter { movie ->
            returnResult(movie)
        }
        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        binding.rvSearch.adapter = adapter
    }

    private fun observeState() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                render(state)
            }
        }
    }

    override fun render(state: SearchState) {
        when (state) {
            is SearchState.Loading -> {
                showLoading(true)
            }
            is SearchState.Success -> {
                showLoading(false)
                adapter.submitList(state.movies)
                showEmptyState(false)
            }
            is SearchState.Error -> {
                showLoading(false)
                Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                showEmptyState(true)
            }
            is SearchState.Empty -> {
                showLoading(false)
                showEmptyState(true)
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