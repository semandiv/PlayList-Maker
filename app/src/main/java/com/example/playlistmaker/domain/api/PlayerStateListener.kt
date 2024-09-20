package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.PlayerState

interface PlayerStateListener {
    fun onPlayerStateChanged(state: PlayerState)
}