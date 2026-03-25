package com.example.wewatch.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wewatch.R
import com.example.wewatch.data.local.MovieEntity

class MovieAdapter(
    private val onCheckboxChanged: (MovieEntity, Boolean) -> Unit
) : ListAdapter<MovieEntity, MovieAdapter.ViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPoster: ImageView = itemView.findViewById(R.id.ivPoster)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvYear: TextView = itemView.findViewById(R.id.tvYear)
        private val cbSelect: CheckBox = itemView.findViewById(R.id.cbSelect)

        fun bind(movie: MovieEntity) {
            tvTitle.text = movie.title
            tvYear.text = movie.year
            Glide.with(itemView.context).load(movie.poster).into(ivPoster)

            cbSelect.setOnCheckedChangeListener(null)
            cbSelect.isChecked = false
            cbSelect.setOnCheckedChangeListener { _, isChecked ->
                onCheckboxChanged(movie, isChecked)
            }
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<MovieEntity>() {
        override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem == newItem
        }
    }
}