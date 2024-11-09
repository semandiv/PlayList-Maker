package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.data.dto.TrackDTO
import com.example.playlistmaker.search.domain.models.Track

class TrackMapper {
    fun map(trackDTO: TrackDTO): Track {
        return Track(
            trackDTO.trackId,
            trackDTO.trackName,
            trackDTO.artistName,
            trackDTO.trackTimeMillis,
            trackDTO.artworkUrl100,
            trackDTO.previewUrl,
            trackDTO.releaseDate,
            trackDTO.primaryGenreName,
            trackDTO.country,
            trackDTO.collectionName
        )
    }
}