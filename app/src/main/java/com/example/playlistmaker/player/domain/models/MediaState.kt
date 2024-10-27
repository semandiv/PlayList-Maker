package com.example.playlistmaker.player.domain.models

sealed class MediaState {
    data class Prepared(val defTime: String): MediaState()
    data object Playing: MediaState()
    data object Paused: MediaState()
    data object Default: MediaState()
}