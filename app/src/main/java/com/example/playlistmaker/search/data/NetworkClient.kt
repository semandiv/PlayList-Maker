package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.network.NetworkResult

interface NetworkClient {
    fun <T> doRequest(dto: Any, responseType: Class<T>): NetworkResult<List<T>>
}