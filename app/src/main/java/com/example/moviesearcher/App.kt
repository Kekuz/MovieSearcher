package com.example.moviesearcher

import android.app.Application
import com.example.moviesearcher.presentation.movies.MoviesSearchPresenter

class App : Application() {

    var moviesSearchPresenter : MoviesSearchPresenter? = null

}