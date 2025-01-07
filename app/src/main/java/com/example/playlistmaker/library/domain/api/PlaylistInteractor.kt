package com.example.playlistmaker.library.domain.api

import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.domain.models.SaveCoverResult
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getAllPlaylists(): Flow<List<Playlist>>
    fun getPlaylist(id: Int): Flow<Playlist>

    suspend fun createPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun setPlaylistName(id: Int, name: String)
    suspend fun setPlaylistDescription(id: Int, description: String)
    suspend fun setPlaylistImage(id: Int, image: String)
    suspend fun addTracksToPlaylist(id: Int, tracks: List<String>, count: Int)
    suspend fun setTrackCount(id: Int, count: Int)

    suspend fun saveImageToStorage(image: String): Flow<SaveCoverResult>
}