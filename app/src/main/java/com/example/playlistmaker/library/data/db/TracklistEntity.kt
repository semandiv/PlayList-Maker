package com.example.playlistmaker.library.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracklist_table")
class TracklistEntity(
    @PrimaryKey val trackId: String,
    val trackName: String,
    val artistName: String?,
    val trackTimeMillis: String?,
    val artworkUrl100: String?,
    val previewUrl: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val collectionName: String?,
    var timeStamp: Long = System.currentTimeMillis()
)