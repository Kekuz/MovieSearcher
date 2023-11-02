package com.example.moviesearcher.data

import com.example.moviesearcher.data.dto.MoviesSearchRequest
import com.example.moviesearcher.data.dto.MoviesSearchResponse
import com.example.moviesearcher.domain.api.MoviesRepository
import com.example.moviesearcher.domain.models.Movie
import com.example.moviesearcher.domain.models.Poster

class MoviesRepositoryImpl(private val networkClient: NetworkClient) : MoviesRepository {

    override fun searchMovies(expression: String): List<Movie> {
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as MoviesSearchResponse).docs.map {
                Movie(it.name, Poster(it.poster.url, it.poster.previewUrl), it.description) }
        } else {
            return emptyList()
        }
    }
}