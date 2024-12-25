package com.example.playlistmaker.library.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) var plID: Int,
    var plName: String,
    var plDescription: String = String(),
    var plImage: String = String(),
    var tracks: List<String> = emptyList(),
    var trackCount: Int = 0
)