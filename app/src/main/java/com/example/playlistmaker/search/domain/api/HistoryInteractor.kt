package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface HistoryInteractor {
    fun addTrack(track: Track)
    fun clearTrack()
    fun getTrack(): List<Track>
}