package com.example.moviesearcher.data.network

import com.example.moviesearcher.data.NetworkClient
import com.example.moviesearcher.data.dto.MoviesSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.moviesearcher.data.dto.Response

class RetrofitNetworkClient : NetworkClient {

    private val baseUrl = "https://api.kinopoisk.dev"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (dto is MoviesSearchRequest) {
            val resp = service.findMovies(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}