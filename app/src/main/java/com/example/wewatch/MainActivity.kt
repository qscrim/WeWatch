package com.example.wewatch

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wewatch.data.local.MovieEntity
import com.example.wewatch.databinding.ActivityMainBinding
import com.example.wewatch.ui.add.AddActivity
import com.example.wewatch.ui.main.MainViewModel
import com.example.wewatch.ui.main.MainViewModelFactory
import com.example.wewatch.ui.main.MovieAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MovieAdapter
    private val selectedMovies = mutableSetOf<MovieEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация ViewModel с Factory
        val factory = MainViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        setupRecyclerView()
        observeMovies()

        binding.fabAdd.setOnClickListener {
            val intent = android.content.Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = MovieAdapter { movie, isChecked ->
            if (isChecked) selectedMovies.add(movie) else selectedMovies.remove(movie)
        }
        binding.rvMovies.layoutManager = LinearLayoutManager(this)
        binding.rvMovies.adapter = adapter
    }

    private fun observeMovies() {
        viewModel.movies.observe(this) { movies ->
            adapter.submitList(movies)
            updateEmptyState(movies.isEmpty())
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.rvMovies.visibility = if (isEmpty) View.GONE else View.VISIBLE
        binding.tvEmptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.ivEmptyImage.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete_selected) {
            deleteSelected()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteSelected() {
        if (selectedMovies.isEmpty()) return
        viewModel.deleteMovies(selectedMovies.toList())
        selectedMovies.clear()
    }
}