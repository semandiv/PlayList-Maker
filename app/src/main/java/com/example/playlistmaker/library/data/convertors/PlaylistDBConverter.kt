package com.example.playlistmaker.library.data.convertors

import com.example.playlistmaker.library.data.db.PlaylistEntity
import com.example.playlistmaker.library.domain.models.Playlist

class PlaylistDBConverter {

    fun map (playlist: Playlist): PlaylistEntity {
      return PlaylistEntity(
          playlist.plID,
          playlist.plName,
          playlist.plDescription,
          playlist.plImage,
          playlist.tracks,
          playlist.trackCount
      )
    }

    fun map (playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            playlistEntity.plID,
            playlistEntity.plName,
            playlistEntity.plDescription,
            playlistEntity.plImage,
            playlistEntity.tracks,
            playlistEntity.trackCount
        )
    }
}