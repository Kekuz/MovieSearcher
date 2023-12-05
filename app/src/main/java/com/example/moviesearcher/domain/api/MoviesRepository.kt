package com.example.moviesearcher.domain.api

import com.example.moviesearcher.domain.models.Movie
import com.example.moviesearcher.util.Resource

interface MoviesRepository {
    fun searchMovies(expression: String): Resource<List<Movie>>
}