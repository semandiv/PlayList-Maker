package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.domain.models.Track

class TrackMapper {
    fun map(response: TracksResponse): List<Track> {
        return response.results.map {
            Track(
                it.trackId,
                it.trackName,
                it.artistName,
                it.trackTimeMillis,
                it.artworkUrl100,
                it.previewUrl,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.collectionName
            )
        }
    }
}