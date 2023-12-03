package com.example.moviesearcher

import android.app.Activity
import android.content.Context
import com.example.moviesearcher.data.MoviesRepositoryImpl
import com.example.moviesearcher.data.network.RetrofitNetworkClient
import com.example.moviesearcher.domain.api.MoviesInteractor
import com.example.moviesearcher.domain.api.MoviesRepository
import com.example.moviesearcher.domain.impl.MoviesInteractorImpl
import com.example.moviesearcher.presentation.movies.MoviesSearchPresenter
import com.example.moviesearcher.presentation.movies.MoviesView
import com.example.moviesearcher.presentation.poster.PosterPresenter
import com.example.moviesearcher.presentation.poster.PosterView
import com.example.moviesearcher.ui.movies.MoviesAdapter

object Creator {
    private fun getMoviesRepository(): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMoviesInteractor(): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository())
    }

    fun provideMoviesSearchPresenter(
        moviesView: MoviesView,
        context: Context,
    ): MoviesSearchPresenter {
        return MoviesSearchPresenter(moviesView, context)
    }

    fun providePosterPresenter(posterView: PosterView, url:String): PosterPresenter {
        return PosterPresenter(posterView, url)
    }
}