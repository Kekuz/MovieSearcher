package com.example.moviesearcher.data

import com.example.moviesearcher.data.dto.MoviesSearchRequest
import com.example.moviesearcher.data.dto.MoviesSearchResponse
import com.example.moviesearcher.domain.api.MoviesRepository
import com.example.moviesearcher.domain.models.Movie
import com.example.moviesearcher.domain.models.Poster
import com.example.moviesearcher.util.Resource

class MoviesRepositoryImpl(private val networkClient: NetworkClient) : MoviesRepository {

    override fun searchMovies(expression: String): Resource<List<Movie>> {
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                Resource.Success((response as MoviesSearchResponse).docs.map {
                    Movie(it.name, Poster(it.poster.url, it.poster.previewUrl), it.description)
                })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}