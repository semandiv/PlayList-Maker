package com.example.playlistmaker

import java.io.Serializable

data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String?,
    val trackTimeMillis: String?,
    val artworkUrl100: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val collectionName: String?
) : Serializable