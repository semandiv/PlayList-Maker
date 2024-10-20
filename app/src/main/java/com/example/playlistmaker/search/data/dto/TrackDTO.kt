package com.example.playlistmaker.search.data.dto

import java.io.Serializable

data class TrackDTO(
    val trackId: String,
    val trackName: String,
    val artistName: String?,
    val trackTimeMillis: String?,
    val artworkUrl100: String?,
    val previewUrl: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val collectionName: String?
) : Serializable