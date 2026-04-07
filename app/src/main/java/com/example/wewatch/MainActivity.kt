package com.example.wewatch

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wewatch.data.local.MovieEntity
import com.example.wewatch.databinding.ActivityMainBinding
import com.example.wewatch.ui.add.AddActivity
import com.example.wewatch.ui.base.MviView
import com.example.wewatch.ui.main.MainIntent
import com.example.wewatch.ui.main.MainState
import com.example.wewatch.ui.main.MainViewModel
import com.example.wewatch.ui.main.MainViewModelFactory
import com.example.wewatch.ui.main.MovieAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), MviView<MainState> {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MovieAdapter
    private val selectedMovies = mutableSetOf<MovieEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация ViewModel
        val factory = MainViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        setupRecyclerView()
        observeState()

        // Отправляем Initial Intent
        viewModel.processIntent(MainIntent.LoadMovies)

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
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

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                render(state)
            }
        }
    }

    override fun render(state: MainState) {
        when (state) {
            is MainState.Loading -> {
                showLoading(true)
            }
            is MainState.Success -> {
                showLoading(false)
                adapter.submitList(state.movies)
                updateEmptyState(false)
            }
            is MainState.Error -> {
                showLoading(false)
                updateEmptyState(true)
            }
            is MainState.Empty -> {
                showLoading(false)
                updateEmptyState(true)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.pbLoading.visibility = if (show) View.VISIBLE else View.GONE
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

    override fun onResume() {
        super.onResume()
        // Принудительно обновляем список при возврате в активность
        viewModel.processIntent(MainIntent.LoadMovies)
    }

    private fun deleteSelected() {
        if (selectedMovies.isEmpty()) return
        viewModel.processIntent(MainIntent.DeleteMovies(selectedMovies.toList()))
        selectedMovies.clear()
    }
}