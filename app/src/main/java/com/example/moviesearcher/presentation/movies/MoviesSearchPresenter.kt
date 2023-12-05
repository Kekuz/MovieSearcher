package com.example.moviesearcher.presentation.movies

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.moviesearcher.util.Creator
import com.example.moviesearcher.R
import com.example.moviesearcher.domain.api.MoviesInteractor
import com.example.moviesearcher.domain.models.Movie
import com.example.moviesearcher.ui.models.MoviesState

class MoviesSearchPresenter(
    private val context: Context,
) {

    private val moviesInteractor = Creator.provideMoviesInteractor(context)

    private var view: MoviesView? = null
    private var state: MoviesState? = null
    private var latestSearchText: String? = null

    fun attachView(view: MoviesView) {
        this.view = view
        state?.let { view.render(it) }
    }

    fun detachView() {
        this.view = null
    }


    private val movies = ArrayList<Movie>()

    private val handler = Handler(Looper.getMainLooper())

    private var lastSearchText: String? = null

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        searchRequest(newSearchText)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText

        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }


    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(MoviesState.Loading)

            moviesInteractor.searchMovies(
                newSearchText,
                object : MoviesInteractor.MoviesConsumer {
                    override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {
                        handler.post {
                            if (foundMovies != null) {
                                movies.clear()
                                movies.addAll(foundMovies)
                            }

                            when {
                                errorMessage != null -> {
                                    renderState(MoviesState.Error(context.getString(R.string.something_went_wrong)))
                                    view?.showToast(errorMessage)
                                }

                                movies.isEmpty() -> {
                                    renderState(MoviesState.Empty(context.getString(R.string.nothing_found)))
                                }

                                else -> {
                                    renderState(MoviesState.Content(movies))
                                }
                            }

                        }
                    }
                })
        }
    }

    private fun renderState(state: MoviesState) {
        this.state = state
        this.view?.render(state)
    }
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}