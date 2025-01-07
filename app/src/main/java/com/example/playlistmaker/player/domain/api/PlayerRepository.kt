package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.search.domain.models.Track

interface PlayerRepository {
    fun setPlayerStateListener(listener: (PlayerState) -> Unit)
    fun sendTrack(): Track?
    fun preparePlayer()
    fun play()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean
    fun updatePlayer()
}