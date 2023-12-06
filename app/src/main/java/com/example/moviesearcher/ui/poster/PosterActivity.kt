package com.example.moviesearcher.ui.poster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.moviesearcher.R
import com.example.moviesearcher.presentation.poster.PosterViewModel

class PosterActivity : AppCompatActivity(){
    private lateinit var viewModel: PosterViewModel
    private lateinit var poster: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = intent.extras?.getString("poster", "") ?: ""

        setContentView(R.layout.activity_poster)
        poster = findViewById(R.id.poster)

        viewModel = ViewModelProvider(
            this,
            PosterViewModel.getViewModelFactory(url)
        )[PosterViewModel::class.java]

        viewModel.observeUrl().observe(this){
            setupPosterImage(it)
        }

    }

    private fun setupPosterImage(url: String) {
        Glide.with(applicationContext)
            .load(url)
            .into(poster)
    }
}