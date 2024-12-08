package com.example.playlistmaker.search.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [TrackEntity::class])
abstract class TrackDatabase: RoomDatabase(){
    abstract fun trackDao(): TrackDao
}