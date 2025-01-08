package com.example.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 6, entities = [PlaylistEntity::class, TracklistEntity::class])
abstract class PlaylistDatabase: RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun tracklistDao(): TracklistDao
}