package com.example.playlistmaker.library.data.convertors

import com.example.playlistmaker.library.data.db.TrackEntity
import com.example.playlistmaker.search.domain.models.Track

class TrackDBConvertor {

    fun map(track: Track): TrackEntity = TrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            previewUrl = track.previewUrl,
            artworkUrl100 = track.artworkUrl100,
            trackTimeMillis = track.trackTimeMillis,
            releaseDate = track.releaseDate,
            collectionName = track.collectionName,
            country = track.country,
            primaryGenreName = track.primaryGenreName,
            timeStamp = track.timeStamp
        )

    fun map(trackEntity: TrackEntity): Track = Track(
            trackId = trackEntity.trackId,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            previewUrl = trackEntity.previewUrl,
            artworkUrl100 = trackEntity.artworkUrl100,
            trackTimeMillis = trackEntity.trackTimeMillis,
            releaseDate = trackEntity.releaseDate,
            collectionName = trackEntity.collectionName,
            country = trackEntity.country,
            primaryGenreName = trackEntity.primaryGenreName,
            timeStamp = trackEntity.timeStamp
        )
}