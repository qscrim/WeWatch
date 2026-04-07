package com.example.wewatch.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wewatch.R
import com.example.wewatch.data.remote.MovieSearchResult

class SearchAdapter(
    private val onItemClick: (MovieSearchResult) -> Unit
) : ListAdapter<MovieSearchResult, SearchAdapter.ViewHolder>(SearchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPoster: ImageView = itemView.findViewById(R.id.ivPoster)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvYear: TextView = itemView.findViewById(R.id.tvYear)
        private val tvType: TextView = itemView.findViewById(R.id.tvType)

        fun bind(movie: MovieSearchResult) {
            tvTitle.text = movie.Title
            tvYear.text = movie.Year
            tvType.text = movie.Type

            Glide.with(itemView.context).load(movie.Poster).into(ivPoster)

            itemView.setOnClickListener {
                onItemClick(movie)
                true
            }
        }
    }

    class SearchDiffCallback : DiffUtil.ItemCallback<MovieSearchResult>() {
        override fun areItemsTheSame(oldItem: MovieSearchResult, newItem: MovieSearchResult): Boolean {
            return oldItem.imdbID == newItem.imdbID
        }

        override fun areContentsTheSame(oldItem: MovieSearchResult, newItem: MovieSearchResult): Boolean {
            return oldItem == newItem
        }
    }
}