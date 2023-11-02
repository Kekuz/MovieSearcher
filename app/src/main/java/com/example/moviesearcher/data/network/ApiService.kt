package com.example.moviesearcher.data.network

import com.example.moviesearcher.data.dto.MoviesSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers(
        "accept: application/json",
        "X-API-KEY: MR7JD2W-Q1MMX9E-PVMXSNB-9VS18XT",
    )
    @GET("/v1.3/movie")
    fun findMovies(
        @Query("name") name: String,
    ): Call<MoviesSearchResponse>
}