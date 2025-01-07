package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.domain.models.SaveCoverResult
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
): PlaylistInteractor {

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override fun getPlaylist(id: Int): Flow<Playlist> {
        return playlistRepository.getPlaylist(id)
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistRepository.addNewPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistRepository.deletePlaylist(playlist)
    }

    override suspend fun setPlaylistName(id: Int, name: String) {
        playlistRepository.setName(id, name)
    }

    override suspend fun setPlaylistDescription(id: Int, description: String) {
        playlistRepository.setDescription(id, description)
    }

    override suspend fun setPlaylistImage(id: Int, image: String) {
        playlistRepository.setImage(id, image)
    }

    override suspend fun addTracksToPlaylist(id: Int, tracks: List<String>, count: Int) {
        playlistRepository.setTracks(id, tracks, count)
    }

    override suspend fun setTrackCount(id: Int, count: Int) {
        playlistRepository.setTrackCount(id, count)
    }

    override suspend fun saveImageToStorage(image: String): Flow<SaveCoverResult> {
        return playlistRepository.saveImageToStorage(image)
    }
}