package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.network.NetworkResult

interface NetworkClient {
    fun doRequest(dto: Any): NetworkResult
}