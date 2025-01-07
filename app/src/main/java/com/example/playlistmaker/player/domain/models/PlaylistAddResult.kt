package com.example.playlistmaker.player.domain.models

sealed class PlaylistAddResult {
    data class Success(val playlistName: String) : PlaylistAddResult()
    data class AlreadyExists(val playlistName: String) : PlaylistAddResult()
    data object Error : PlaylistAddResult()
    data object TrackIdNotFound : PlaylistAddResult()
}