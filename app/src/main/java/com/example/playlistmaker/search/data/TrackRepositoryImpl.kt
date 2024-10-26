package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackRequest
import com.example.playlistmaker.search.data.network.NetworkResult
import com.example.playlistmaker.search.domain.api.TrackRepository

class TrackRepositoryImpl(private val networkClient: NetworkClient) :
    TrackRepository {
    override fun searchTracks(query: String): NetworkResult {
        return networkClient.doRequest(TrackRequest(query))
    }
}