package com.example.playlistmaker.search.data.convertors

import com.example.playlistmaker.search.data.db.TrackEntity
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
            track.primaryGenreName
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
            trackEntity.primaryGenreName
        )
    }
}