package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface ToPlayerInteractor {
    fun toPlayer(track: Track)
}