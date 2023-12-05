package com.example.moviesearcher.presentation.movies

import com.example.moviesearcher.ui.models.MoviesState
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

interface MoviesView: MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun render(state: MoviesState)
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showToast(additionalMessage: String)
}