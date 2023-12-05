package com.example.moviesearcher.presentation.movies

import com.example.moviesearcher.domain.models.Movie
import com.example.moviesearcher.ui.models.MoviesState

interface MoviesView {
    // Методы, меняющие внешний вид экрана

    fun render(state: MoviesState)

    // Методы «одноразовых событий»

    fun showToast(additionalMessage: String)
}