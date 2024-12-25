package com.example.playlistmaker.library.data

import com.example.playlistmaker.library.data.convertors.PlaylistDBConverter
import com.example.playlistmaker.library.data.db.PlaylistDao
import com.example.playlistmaker.library.data.db.PlaylistEntity
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val converter: PlaylistDBConverter
): PlaylistRepository {

    override fun getPlaylists(): Flow<List<Playlist>> = playlistDao.getPlaylists()
        .map { playlistEntities -> convertFromDB(playlistEntities)  }

    override fun getPlaylist(id: Int): Flow<Playlist> = flow{
        val playlist = playlistDao.getPlaylist(id)
        emit(playlist)
    }

    override suspend fun addNewPlaylist(playlist: Playlist) {
        playlistDao.insertPlaylist(plToDB(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.deletePlaylist(plToDB(playlist))
    }

    override suspend fun setName(id: Int, name: String) {
        playlistDao.updatePlName(id, name)
    }

    override suspend fun setDescription(id: Int, description: String) {
        playlistDao.updatePlDescription(id, description)
    }

    override suspend fun setImage(id: Int, image: String) {
        playlistDao.updatePlImage(id, image)
    }

    override suspend fun setTracks(id: Int, tracks: List<String>) {
        playlistDao.updatePlTracks(id, tracks)
    }

    override suspend fun setTrackCount(id: Int, trackCountMethod: Int) {
        playlistDao.updatePlTrackCount(id, trackCountMethod)
    }

    private fun convertFromDB(entity: List<PlaylistEntity>): List<Playlist> {
        return entity.map { converter.map(it) }
    }

    private fun plToDB(playlist: Playlist): PlaylistEntity {
        return converter.map(playlist)
    }
}