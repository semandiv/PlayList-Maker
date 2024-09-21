package com.example.playlistmaker.presenter.api

import com.example.playlistmaker.domain.models.PlayerState

interface PlayTrack {
    fun preparePlayer()
    fun play()
    fun pause()
    fun releasePlayer()
    fun getPlayerState(): PlayerState
    fun currentPosition(): Int
}