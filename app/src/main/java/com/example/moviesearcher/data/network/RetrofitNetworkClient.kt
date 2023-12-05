package com.example.moviesearcher.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.moviesearcher.data.NetworkClient
import com.example.moviesearcher.data.dto.MoviesSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.moviesearcher.data.dto.Response

class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val baseUrl = "https://api.kinopoisk.dev"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ApiService::class.java)

    override fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        if (dto !is MoviesSearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        val response = service.findMovies(dto.expression).execute()
        val body = response.body()
        return body?.apply {
            resultCode = response.code()
        } ?: Response().apply {
            resultCode = response.code()
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}