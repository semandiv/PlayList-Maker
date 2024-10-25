package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.models.MediaState

interface MediaStateListener {
    fun onMediaStateListener(mediaState: MediaState)
}