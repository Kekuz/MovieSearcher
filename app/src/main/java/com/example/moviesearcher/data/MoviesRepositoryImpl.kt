package com.example.moviesearcher.data

import android.util.Log
import com.example.moviesearcher.data.dto.MoviesSearchRequest
import com.example.moviesearcher.data.dto.MoviesSearchResponse
import com.example.moviesearcher.domain.api.MoviesRepository
import com.example.moviesearcher.domain.models.Movie
import com.example.moviesearcher.domain.models.Poster
import com.example.moviesearcher.util.Resource

class MoviesRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage,
) : MoviesRepository {

    override fun searchMovies(expression: String): Resource<List<Movie>> {
        val response = networkClient.doRequest(MoviesSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                Log.e("Films",(response as MoviesSearchResponse).docs.toString())
                Resource.Success((response as MoviesSearchResponse).docs.map {
                    Movie(
                        it.id,
                        it.name ?: "-",
                        Poster(
                            it.poster?.url ?: "-",
                            it.poster?.previewUrl ?: "-"
                        ),
                        it.description ?: "-",
                        localStorage.getSavedFavorites().contains(it.id),
                    )
                })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
    override fun addMovieToFavorites(movie: Movie) {
        localStorage.addToFavorites(movie.id)
    }

    override fun removeMovieFromFavorites(movie: Movie) {
        localStorage.removeFromFavorites(movie.id)
    }
}