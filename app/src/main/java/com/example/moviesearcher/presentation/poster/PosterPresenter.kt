package com.example.moviesearcher.presentation.poster

import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.moviesearcher.R

class PosterPresenter(
    private val view: PosterView,
    private val url: String
) {
    fun onCreate() {
        view.setupPosterImage(url)
    }
}