package com.example.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 5, entities = [PlaylistEntity::class])
abstract class PlaylistDatabase: RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}