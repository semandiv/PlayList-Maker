package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface TrackPlayRepository {
    fun setTrackToPlay(track: Track)
}