package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackRequest
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.TrackSearchResult

class TrackRepositoryImpl(private val networkClient: NetworkClient, private val trackMapper: TrackMapper) : TrackRepository {
    override fun searchTracks(query: String): TrackSearchResult {
        val response = networkClient.doRequest(TrackRequest(query))
        return when (response.resultCode) {
            200 -> {TrackSearchResult(response.resultCode, trackMapper.map((response as TracksResponse).results))}
            400 -> {TrackSearchResult(response.resultCode, emptyList())}
            else -> {TrackSearchResult(response.resultCode, emptyList())}
        }
    }
}