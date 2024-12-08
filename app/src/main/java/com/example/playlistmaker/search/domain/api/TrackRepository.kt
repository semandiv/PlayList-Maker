package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.data.network.NetworkResult

interface TrackRepository {
    fun searchTracks(query: String): NetworkResult
}