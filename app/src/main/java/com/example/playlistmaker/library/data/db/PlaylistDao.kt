package com.example.playlistmaker.library.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist ORDER BY plID DESC")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("UPDATE playlist SET plDescription = :description WHERE plID = :id ")
    suspend fun updatePlDescription(id: Int, description: String)

    @Query("UPDATE playlist SET plName = :name WHERE plID = :id")
    suspend fun updatePlName(id: Int, name: String)

    @Query("UPDATE playlist SET plImage = :image WHERE plID = :id")
    suspend fun updatePlImage(id: Int, image: String)

    @Query("UPDATE playlist SET tracks = :tracks, trackCount = :trackCount WHERE plID = :id")
    suspend fun updatePlTracks(id: Int, tracks: String, trackCount: Int)

    @Query("UPDATE playlist SET trackCount = :trackCount WHERE plID = :id")
    suspend fun updatePlTrackCount(id: Int, trackCount: Int)

    @Query("SELECT * FROM playlist WHERE plID = :id")
    suspend fun getPlaylist(id: Int): Playlist
}