package com.example.playlistmaker.player.domain.models

sealed class MediaState {
    object Prepared: MediaState()
    object Playing: MediaState()
    object Paused: MediaState()
    object Default: MediaState()
}