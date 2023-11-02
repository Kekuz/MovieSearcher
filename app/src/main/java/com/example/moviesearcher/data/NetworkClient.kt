package com.example.moviesearcher.data

import com.example.moviesearcher.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}