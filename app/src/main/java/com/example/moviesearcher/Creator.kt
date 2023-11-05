package com.example.moviesearcher

import com.example.moviesearcher.data.MoviesRepositoryImpl
import com.example.moviesearcher.data.network.RetrofitNetworkClient
import com.example.moviesearcher.domain.api.MoviesInteractor
import com.example.moviesearcher.domain.api.MoviesRepository
import com.example.moviesearcher.domain.impl.MoviesInteractorImpl

object Creator {
    private fun getMoviesRepository(): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMoviesInteractor(): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository())
    }
}