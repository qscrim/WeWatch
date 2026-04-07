package com.example.wewatch.ui.add

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.wewatch.data.local.MovieEntity
import com.example.wewatch.databinding.ActivityAddBinding
import com.example.wewatch.domain.model.Movie
import com.example.wewatch.ui.base.MviView
import com.example.wewatch.ui.search.SearchActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity(), MviView<AddState> {

    private lateinit var binding: ActivityAddBinding
    private lateinit var viewModel: AddViewModel

    private var currentMovie: Movie? = null

    companion object {
        private const val REQUEST_CODE_SEARCH = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = AddViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[AddViewModel::class.java]

        observeState()
        setupClickListeners()
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                render(state)
            }
        }
    }

    override fun render(state: AddState) {
        when (state) {
            is AddState.Initial -> {
                // Начальное состояние
            }
            is AddState.Loading -> {
                showLoading(true)
            }
            is AddState.Success -> {
                showLoading(false)
                displayMovieDetails(state.movie)
            }
            is AddState.Error -> {
                showLoading(false)
                Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.pbLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun displayMovieDetails(movie: Movie) {
        currentMovie = movie
        binding.etTitle.setText(movie.title)
        binding.etYear.setText(movie.year)

        showMovieDetailsSimple(movie.title, movie.year, movie.poster, movie.genre ?: "")
    }

    private fun showMovieDetailsSimple(title: String, year: String, poster: String?, genre: String) {
        binding.ivPoster.visibility = View.VISIBLE
        binding.tvResultInfo.visibility = View.VISIBLE
        binding.btnAddMovie.visibility = View.VISIBLE

        if (!poster.isNullOrBlank()) {
            com.bumptech.glide.Glide.with(this).load(poster).into(binding.ivPoster)
        }
        binding.tvResultInfo.text = "$title ($year)\n$genre"
    }

    private fun setupClickListeners() {
        binding.btnSearch.setOnClickListener {
            val query = binding.etTitle.text.toString()
            val year = binding.etYear.text.toString()

            val intent = Intent(this, SearchActivity::class.java).apply {
                putExtra("QUERY", query)
                putExtra("YEAR", year)
            }
            startActivityForResult(intent, REQUEST_CODE_SEARCH)
        }

        binding.btnAddMovie.setOnClickListener {
            saveMovie()
        }
    }

    private fun saveMovie() {
        val title = binding.etTitle.text.toString()
        val year = binding.etYear.text.toString()

        if (title.isBlank() || currentMovie == null) {
            Toast.makeText(this, "Заполните название фильма", Toast.LENGTH_SHORT).show()
            return
        }

        val movie = Movie(
            id = currentMovie!!.id,
            title = title,
            year = year,
            poster = currentMovie!!.poster,
            genre = currentMovie!!.genre
        )

        viewModel.processIntent(AddIntent.SaveMovie(movie))
        Toast.makeText(this, "Фильм добавлен!", Toast.LENGTH_SHORT).show()
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SEARCH && resultCode == RESULT_OK) {
            val title = data?.getStringExtra("RESULT_TITLE")
            val year = data?.getStringExtra("RESULT_YEAR")
            val poster = data?.getStringExtra("RESULT_POSTER")
            val id = data?.getStringExtra("RESULT_ID")
            val genre = data?.getStringExtra("RESULT_TYPE")

            if (title != null && id != null) {
                currentMovie = Movie(
                    id = id,
                    title = title,
                    year = year ?: "",
                    poster = poster,
                    genre = genre
                )

                binding.etTitle.setText(title)
                binding.etYear.setText(year ?: "")

                showMovieDetailsSimple(title, year ?: "", poster, genre ?: "")
            }
        }
    }
}