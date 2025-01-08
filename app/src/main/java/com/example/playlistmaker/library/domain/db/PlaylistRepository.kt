package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.domain.models.SaveCoverResult
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    fun getPlaylists(): Flow<List<Playlist>>
    fun getPlaylist(id: Int): Flow<Playlist>

    suspend fun addNewPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun setName(id: Int, name: String)
    suspend fun setDescription(id: Int, description: String)
    suspend fun setImage(id: Int, image: String)
    suspend fun setTracks(id: Int, tracks: List<String>, count: Int, track: Track)
    suspend fun setTrackCount(id: Int, trackCountMethod: Int)
    suspend fun saveImageToStorage(image: String):Flow<SaveCoverResult>

}