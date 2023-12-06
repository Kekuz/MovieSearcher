package com.example.moviesearcher.data.dto


data class MovieDto(
    val id: String,
    val name: String?,
    val poster: PosterDto?,
    val description: String?,
)
