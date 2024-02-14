package com.example.moviesearcher.ui.movies

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesearcher.ui.poster.PosterActivity
import com.example.moviesearcher.databinding.ActivityMoviesBinding
import com.example.moviesearcher.domain.models.Movie
import com.example.moviesearcher.presentation.movies.MoviesSearchViewModel
import com.example.moviesearcher.presentation.movies.ToastState
import com.example.moviesearcher.ui.models.MoviesState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MoviesActivity : ComponentActivity() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var viewModel: MoviesSearchViewModel
    private lateinit var binding: ActivityMoviesBinding


    private val adapter = MoviesAdapter(
        object : MoviesAdapter.MovieClickListener {
            override fun onMovieClick(movie: Movie) {
                if (clickDebounce()) {
                    val intent = Intent(this@MoviesActivity, PosterActivity::class.java)
                    intent.putExtra("poster", movie.poster.previewUrl)
                    startActivity(intent)
                }
            }

            override fun onFavoriteToggleClick(movie: Movie) {
                viewModel.toggleFavorite(movie)
            }

        }
    )

    private var isClickAllowed = true

    //private val handler = Handler(Looper.getMainLooper())

    private var textWatcher: TextWatcher? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.moviesList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        viewModel = ViewModelProvider(
            this,
            MoviesSearchViewModel.getViewModelFactory()
        )[MoviesSearchViewModel::class.java]

        binding.moviesList.adapter = adapter

        viewModel.observeState().observe(this) {
            render(it)
        }

        viewModel.observeToastState().observe(this) { toastState ->
            if (toastState is ToastState.Show) {
                showToast(toastState.additionalMessage)
                viewModel.toastWasShown()
            }
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let { binding.queryInput.addTextChangedListener(it) }

    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { binding.queryInput.removeTextChangedListener(it) }
    }

    private fun render(state: MoviesState) {
        when (state) {
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Empty -> showEmpty(state.message)
            is MoviesState.Error -> showError(state.errorMessage)
            MoviesState.Loading -> showLoading()
        }
    }


    private fun showLoading() {
        binding.moviesList.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        binding.moviesList.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE

        binding.placeholderMessage.text = errorMessage
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    private fun showContent(movies: List<Movie>) {
        binding.moviesList.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        adapter.movies.clear()
        adapter.movies.addAll(movies)
        adapter.notifyDataSetChanged()
    }

    private fun showToast(additionalMessage: String) {
        Toast.makeText(this, additionalMessage, Toast.LENGTH_LONG).show()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current

    }

}