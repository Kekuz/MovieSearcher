package com.example.moviesearcher

import com.example.moviesearcher.models.MoviesSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers(
        "accept: application/json",
        "X-API-KEY: MR7JD2W-Q1MMX9E-PVMXSNB-9VS18XT",
    )
    @GET("/v1.2/movie/search")
    fun findMovies(
        @Query("page") page: String,
        @Query("limit") limit: String,
        @Query("query") query: String,
    ): Call<MoviesSearchResponse>



    @Headers(
        "accept: application/json",
        "X-API-KEY: MR7JD2W-Q1MMX9E-PVMXSNB-9VS18XT",
    )
    @GET("/v1.3/movie")
    fun findMoviesCustom(
        //@Query("sortField") sortField: String,
        //@Query("selectFields") selectFields: String,
        //@Query("page") page: String,
        //@Query("limit") limit: String,
        @Query("name") name: String,
        @Query("poster.url") poster: String,
    ): Call<MoviesSearchResponse>


    @Headers(
        "accept: application/json",
        "X-API-KEY: MR7JD2W-Q1MMX9E-PVMXSNB-9VS18XT",
    )
    @GET("/v1.3/movie/random")
    fun randomMovie(
    ): Call<MoviesSearchResponse>
}