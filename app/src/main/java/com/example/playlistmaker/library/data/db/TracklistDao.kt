package com.example.playlistmaker.library.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TracklistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackToList(track: TracklistEntity)

    @Delete
    suspend fun deleteTrackFromList(track: TracklistEntity)

    @Query("SELECT trackId FROM tracklist_table")
    suspend fun getTrackIdFromList(): List<String>

    @Query("SELECT * FROM tracklist_table ORDER BY timeStamp DESC")
    fun getAllTracksFromList(): Flow<List<TracklistEntity>>
}