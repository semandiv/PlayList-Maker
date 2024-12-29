package com.example.playlistmaker.library.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val plID: Int = 0,
    var plName: String,
    var plDescription: String = String(),
    var plImage: String = String(),
    var tracks: String = String(),
    var trackCount: Int = 0
)