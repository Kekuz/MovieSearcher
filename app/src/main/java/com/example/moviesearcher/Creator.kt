package com.example.moviesearcher

import android.app.Activity
import com.example.moviesearcher.data.MoviesRepositoryImpl
import com.example.moviesearcher.data.network.RetrofitNetworkClient
import com.example.moviesearcher.domain.api.MoviesInteractor
import com.example.moviesearcher.domain.api.MoviesRepository
import com.example.moviesearcher.domain.impl.MoviesInteractorImpl
import com.example.moviesearcher.presentation.MoviesSearchController
import com.example.moviesearcher.presentation.PosterController
import com.example.moviesearcher.ui.movies.MoviesAdapter

object Creator {
    private fun getMoviesRepository(): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMoviesInteractor(): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository())
    }

    fun provideMoviesSearchController(activity: Activity, adapter: MoviesAdapter): MoviesSearchController {
        return MoviesSearchController(activity, adapter)
    }

    fun providePosterController(activity: Activity): PosterController {
        return PosterController(activity)
    }
}