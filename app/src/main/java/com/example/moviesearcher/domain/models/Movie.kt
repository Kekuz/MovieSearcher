package com.example.moviesearcher.domain.models

import java.net.IDN

data class Movie(
    val id: String,
    val name: String,
    val poster: Poster,
    val description: String,
    val inFavorite: Boolean,
)
