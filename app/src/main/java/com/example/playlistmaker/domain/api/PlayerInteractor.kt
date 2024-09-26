package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.PlayerState

interface PlayerInteractor {
    fun preparePlayer()
    fun play()
    fun pause()
    fun releasePlayer()
    fun observePlayerState(listener: (PlayerState) -> Unit)
    fun currentPosition(): Int
}