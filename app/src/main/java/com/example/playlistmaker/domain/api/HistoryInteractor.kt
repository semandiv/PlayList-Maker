package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface HistoryInteractor {
    fun addTrack(track: Track)
    fun clearTrack()
    fun getTrack(): MutableList<Track>
}