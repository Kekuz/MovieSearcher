package com.example.moviesearcher.util

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

object Creator {
    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
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