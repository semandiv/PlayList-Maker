package com.example.playlistmaker.library.data.convertors

import com.example.playlistmaker.library.data.db.PlaylistEntity
import com.example.playlistmaker.library.domain.models.Playlist

class PlaylistDBConverter {

    fun map (playlist: Playlist): PlaylistEntity {
      return PlaylistEntity(
          plID = playlist.plID,
          plName = playlist.plName,
          plDescription = playlist.plDescription,
          plImage = playlist.plImage,
          tracks = playlist.tracks,
          trackCount = playlist.trackCount
      )
    }

    fun map (playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            plID = playlistEntity.plID,
            plName = playlistEntity.plName,
            plDescription = playlistEntity.plDescription,
            plImage = playlistEntity.plImage,
            tracks = playlistEntity.tracks,
            trackCount = playlistEntity.trackCount
        )
    }
}