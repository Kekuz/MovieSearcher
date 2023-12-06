package com.example.moviesearcher.ui.movies

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesearcher.R
import com.example.moviesearcher.domain.models.Movie

class MovieViewHolder(parent: ViewGroup, private val clickListener: MoviesAdapter.MovieClickListener) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_movie, parent, false)
    ) {

    private var cover: ImageView = itemView.findViewById(R.id.cover)
    private var title: TextView = itemView.findViewById(R.id.title)
    private var description: TextView = itemView.findViewById(R.id.description)
    private var inFavoriteToggle: ImageView = itemView.findViewById(R.id.favorite)

    fun bind(movie: Movie) {
        Glide.with(itemView)
            .load(movie.poster.url)
            .into(cover)

        title.text = movie.name
        description.text = movie.description
        inFavoriteToggle.setImageDrawable(getFavoriteToggleDrawable(movie.inFavorite))

        itemView.setOnClickListener { clickListener.onMovieClick(movie) }
        inFavoriteToggle.setOnClickListener { clickListener.onFavoriteToggleClick(movie) }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getFavoriteToggleDrawable(inFavorite: Boolean): Drawable? =
        itemView.context.getDrawable(
            if (inFavorite) R.drawable.remove_circle else R.drawable.add_circle
        )


}