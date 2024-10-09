package com.example.playlistmaker.domain.models

data class TrackSearchResult(
    val resultCode: Int,
    val tracks: List<Track>
)
