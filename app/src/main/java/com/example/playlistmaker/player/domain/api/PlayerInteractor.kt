package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.models.MediaState
import com.example.playlistmaker.search.domain.models.Track

interface PlayerInteractor {
    fun preparePlayer()
    fun play()
    fun pause()
    fun releasePlayer()
    fun observeMediaState(listener: (MediaState) -> Unit)
    fun updateState(newState: MediaState)
    fun currentPosition(): Int
    fun isPlaying(): Boolean
    fun loadTrack(): Track?

}