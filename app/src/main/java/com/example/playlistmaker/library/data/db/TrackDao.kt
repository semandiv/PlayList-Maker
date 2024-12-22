package com.example.playlistmaker.library.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT trackId FROM track_table")
    suspend fun getTrackId(): List<String>

    @Query("SELECT * FROM track_table ORDER BY timeStamp DESC")
    fun getAllTracks(): Flow<List<TrackEntity>>

}