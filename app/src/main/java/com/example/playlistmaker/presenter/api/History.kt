package com.example.playlistmaker.presenter.api

import com.example.playlistmaker.domain.models.Track

interface History {
    fun addTrack(track: Track)
    fun getTracks(): List<Track>
    fun clearHistory()
}