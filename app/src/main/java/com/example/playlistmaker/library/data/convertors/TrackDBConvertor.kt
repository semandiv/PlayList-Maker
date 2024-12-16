package com.example.playlistmaker.library.data.convertors

import com.example.playlistmaker.library.data.db.TrackEntity
import com.example.playlistmaker.search.domain.models.Track

class TrackDBConvertor {

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.previewUrl,
            track.artworkUrl100,
            track.trackTimeMillis,
            track.releaseDate,
            track.collectionName,
            track.country,
            track.primaryGenreName,
            track.isFavorite
        )
    }

    fun map(trackEntity: TrackEntity): Track {
        return Track(
            trackEntity.trackId,
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.previewUrl,
            trackEntity.artworkUrl100,
            trackEntity.trackTimeMillis,
            trackEntity.releaseDate,
            trackEntity.collectionName,
            trackEntity.country,
            trackEntity.primaryGenreName,
            trackEntity.isFavorite
        )
    }
}