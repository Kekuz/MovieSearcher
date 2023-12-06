package com.example.moviesearcher.util

import android.content.Context
import com.example.moviesearcher.data.MoviesRepositoryImpl
import com.example.moviesearcher.data.network.RetrofitNetworkClient
import com.example.moviesearcher.domain.api.MoviesInteractor
import com.example.moviesearcher.domain.api.MoviesRepository
import com.example.moviesearcher.domain.impl.MoviesInteractorImpl

object Creator {
    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }
}