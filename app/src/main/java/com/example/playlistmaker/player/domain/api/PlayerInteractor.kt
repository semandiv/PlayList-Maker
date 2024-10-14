package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.models.PlayerState

interface PlayerInteractor {
    fun preparePlayer()
    fun play()
    fun pause()
    fun releasePlayer()
    fun observePlayerState(listener: (PlayerState) -> Unit)
    fun currentPosition(): Int
}