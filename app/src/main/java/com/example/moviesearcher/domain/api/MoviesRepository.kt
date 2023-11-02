package com.example.moviesearcher.domain.api

import com.example.moviesearcher.domain.models.Movie

interface MoviesRepository {
    fun searchMovies(expression: String): List<Movie>
}