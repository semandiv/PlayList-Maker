package com.example.playlistmaker.domain.api

interface PlayerInteractor {
    fun play()
    fun pause()
    fun releasePlayer()
    fun observePlayerState(listener: PlayerStateListener)
    fun currentPosition(): Int
}