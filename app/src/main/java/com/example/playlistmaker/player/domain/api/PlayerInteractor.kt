package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.search.domain.models.Track

interface PlayerInteractor {
    fun preparePlayer()
    fun play()
    fun pause()
    fun releasePlayer()
    fun observePlayerState(listener: (PlayerState) -> Unit)
    fun currentPosition(): Int
    fun isPlaying(): Boolean
    fun loadTrack(): Track?
}