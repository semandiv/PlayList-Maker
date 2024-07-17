package com.example.playlistmaker

import java.io.Serializable

data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String
) : Serializable {
    override fun toString(): String {
        return "Track(trackId='$trackId', trackName='$trackName', artistName='$artistName', trackTimeMillis='$trackTimeMillis', artworkUrl100='$artworkUrl100')"
    }
}

