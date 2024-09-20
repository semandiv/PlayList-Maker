package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SharedPrefRepository {
    fun addTrack(track: Track)
    fun getTrack(): MutableList<Track>
    fun clearTrack()
}