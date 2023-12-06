package com.example.moviesearcher.presentation.movies

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moviesearcher.util.Creator
import com.example.moviesearcher.R
import com.example.moviesearcher.domain.api.MoviesInteractor
import com.example.moviesearcher.domain.models.Movie
import com.example.moviesearcher.ui.models.MoviesState

class MoviesSearchViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val moviesInteractor = Creator.provideMoviesInteractor(getApplication())

    private val stateLiveData = MutableLiveData<MoviesState>()
    fun observeState(): LiveData<MoviesState> = stateLiveData

    private val toastState = MutableLiveData<ToastState>(ToastState.None)
    fun observeToastState(): LiveData<ToastState> = toastState

    private var latestSearchText: String? = null

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

    override fun onCleared() {
        handler.removeCallbacksAndMessages(searchRunnable)
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(MoviesState.Loading)

            moviesInteractor.searchMovies(
                newSearchText,
                object : MoviesInteractor.MoviesConsumer {
                    override fun consume(foundMovies: List<Movie>?, errorMessage: String?) {

                        if (foundMovies != null) {
                            movies.clear()
                            movies.addAll(foundMovies)
                        }

                        when {
                            errorMessage != null -> {
                                renderState(
                                    MoviesState.Error(
                                        getApplication<Application>().getString(
                                            R.string.something_went_wrong
                                        )
                                    )
                                )

                                showToast(errorMessage)
                            }

                            movies.isEmpty() -> {
                                renderState(
                                    MoviesState.Empty(
                                        getApplication<Application>().getString(
                                            R.string.nothing_found
                                        )
                                    )
                                )
                            }

                            else -> {
                                renderState(MoviesState.Content(movies))
                            }
                        }


                    }
                })
        }
    }

    private fun renderState(state: MoviesState) {
        stateLiveData.postValue(state)
    }

    fun showToast(message: String) {
        toastState.postValue(ToastState.Show(message))
    }

    fun toastWasShown() {
        toastState.value = ToastState.None
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MoviesSearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }
}