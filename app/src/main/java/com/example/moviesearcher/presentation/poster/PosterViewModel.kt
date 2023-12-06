package com.example.moviesearcher.presentation.poster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class PosterViewModel(url: String) : ViewModel(){

    private val urlLiveData = MutableLiveData<String>()
    fun observeUrl(): LiveData<String> = urlLiveData

    init {
        urlLiveData.value = url
    }

    companion object {
        fun getViewModelFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PosterViewModel(url)
            }
        }
    }

}