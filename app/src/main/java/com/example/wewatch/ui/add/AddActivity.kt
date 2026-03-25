package com.example.wewatch.ui.add

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.wewatch.data.local.MovieEntity
import com.example.wewatch.databinding.ActivityAddBinding
import com.example.wewatch.ui.search.SearchActivity

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private lateinit var viewModel: AddViewModel

    private var currentPoster: String = ""
    private var currentId: String = ""
    private var currentGenre: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация ViewModel
        val factory = AddViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[AddViewModel::class.java]

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnSearch.setOnClickListener {
            val title = binding.etTitle.text.toString()
            if (title.isBlank()) {
                Toast.makeText(this, "Введите название", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val year = binding.etYear.text.toString().takeIf { it.isNotBlank() }

            val intent = Intent(this, SearchActivity::class.java).apply {
                putExtra("QUERY", title)
                putExtra("YEAR", year)
            }
            startActivityForResult(intent, 100)
        }

        binding.btnAddMovie.setOnClickListener {
            saveToDatabase()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val title = data?.getStringExtra("RESULT_TITLE")
            val year = data?.getStringExtra("RESULT_YEAR")
            val poster = data?.getStringExtra("RESULT_POSTER")
            val genre = data?.getStringExtra("RESULT_GENRE") ?: ""
            val id = data?.getStringExtra("RESULT_ID")

            if (title != null && id != null) {
                binding.etTitle.setText(title)
                binding.etYear.setText(year ?: "")
                currentPoster = poster ?: ""
                currentId = id
                currentGenre = genre

                showMovieDetailsSimple(title, year ?: "", poster, genre)
            }
        }
    }

    private fun showMovieDetailsSimple(title: String, year: String, poster: String?, genre: String) {
        binding.ivPoster.visibility = View.VISIBLE
        binding.tvResultInfo.visibility = View.VISIBLE
        binding.btnAddMovie.visibility = View.VISIBLE

        if (!poster.isNullOrBlank()) {
            Glide.with(this).load(poster).into(binding.ivPoster)
        }
        binding.tvResultInfo.text = "$title ($year)\n$genre"
    }

    private fun saveToDatabase() {
        if (currentId.isBlank()) {
            Toast.makeText(this, "Сначала выберите фильм через поиск", Toast.LENGTH_SHORT).show()
            return
        }

        val movie = MovieEntity(
            id = currentId,
            title = binding.etTitle.text.toString(),
            year = binding.etYear.text.toString(),
            poster = currentPoster,
            genre = currentGenre
        )

        viewModel.addMovieToDatabase(movie)
        Toast.makeText(this, "Фильм добавлен", Toast.LENGTH_SHORT).show()
        finish()
    }
}