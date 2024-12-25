package com.example.playlistmaker.library.domain.models

data class Playlist(
    val plID: Int,
    var plName: String,
    var plDescription: String = String(),
    var plImage: String = String(),
    var tracks: List<String> = emptyList(),
    var trackCount: Int = 0
)