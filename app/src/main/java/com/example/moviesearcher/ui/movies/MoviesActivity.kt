package com.example.moviesearcher.ui.movies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesearcher.ui.poster.PosterActivity
import com.example.moviesearcher.R
import com.example.moviesearcher.databinding.ActivityMoviesBinding
import com.example.moviesearcher.domain.models.Movie
import com.example.moviesearcher.Creator
import com.example.moviesearcher.domain.api.MoviesInteractor

class MoviesActivity : AppCompatActivity() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
    private lateinit var binding: ActivityMoviesBinding

    private val movies = ArrayList<Movie>()

    private val adapter = MoviesAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", it.poster.previewUrl)
            startActivity(intent)
        }
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { searchRequest() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter.movies = movies

        binding.moviesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.moviesList.adapter = adapter

        binding.queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchDebounce()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }

    private fun searchRequest() {
        if (binding.queryInput.text.isNotEmpty()) {

            binding.placeholderMessage.visibility = View.GONE
            binding.moviesList.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            Creator.provideMoviesInteractor().searchMovies(binding.queryInput.text.toString(), object : MoviesInteractor.MoviesConsumer {
                override fun consume(foundMovies: List<Movie>) {
                    Log.e("Movies", foundMovies.toString())
                    runOnUiThread{
                        movies.clear()
                        if (foundMovies.isNotEmpty()) {
                            binding.moviesList.visibility = View.VISIBLE
                            movies.addAll(foundMovies)
                            adapter.notifyDataSetChanged()
                        }
                        if (foundMovies.isEmpty()) {
                            showMessage(getString(R.string.nothing_found), "")
                        } else {
                            hideMessage()
                        }
                    }
                }
            })
        }
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            binding.placeholderMessage.visibility = View.VISIBLE
            movies.clear()
            adapter.notifyDataSetChanged()
            binding.placeholderMessage.text = additionalMessage
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG) .show()
            }
        } else {
            binding.placeholderMessage.visibility = View.GONE
        }
    }

    private fun hideMessage() {
        binding.placeholderMessage.visibility = View.GONE
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
}