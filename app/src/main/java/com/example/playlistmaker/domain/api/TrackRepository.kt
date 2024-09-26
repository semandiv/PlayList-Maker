package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TrackSearchResult

interface TrackRepository {
    fun searchTracks(query: String): TrackSearchResult
}