package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface SharedPrefRepository {
    fun addTrack(track: Track)
    fun getTrack(): List<Track>
    fun clearTrack()
}