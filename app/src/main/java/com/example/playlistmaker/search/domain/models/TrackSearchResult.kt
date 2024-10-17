package com.example.playlistmaker.search.domain.models

data class TrackSearchResult(
    val resultCode: Int,
    val tracks: List<Track>
)
