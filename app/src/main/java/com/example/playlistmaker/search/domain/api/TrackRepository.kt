package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.data.network.NetworkResult

interface TrackRepository {
    fun <T>searchTracks(query: String, responseType: Class<T>): NetworkResult<List<T>>
}