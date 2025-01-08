package com.example.playlistmaker.library.data.convertors

import com.example.playlistmaker.library.data.db.PlaylistEntity
import com.example.playlistmaker.library.data.db.TracklistEntity
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track

class PlaylistDBConverter {

    fun map(playlist: Playlist): PlaylistEntity = PlaylistEntity(
        plID = playlist.plID,
        plName = playlist.plName,
        plDescription = playlist.plDescription,
        plImage = playlist.plImage,
        tracks = playlist.tracks,
        trackCount = playlist.trackCount
    )


    fun map(playlistEntity: PlaylistEntity): Playlist = Playlist(
        plID = playlistEntity.plID,
        plName = playlistEntity.plName,
        plDescription = playlistEntity.plDescription,
        plImage = playlistEntity.plImage,
        tracks = playlistEntity.tracks,
        trackCount = playlistEntity.trackCount
    )

    fun mapToTracklistEntity(track: Track): TracklistEntity = TracklistEntity(
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

    fun map(trackEntity: TracklistEntity): Track = Track(
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