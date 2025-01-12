package com.example.playlistmaker.library.data

import android.content.Context
import android.net.Uri
import com.example.playlistmaker.library.data.convertors.PlaylistDBConverter
import com.example.playlistmaker.library.data.db.PlaylistDao
import com.example.playlistmaker.library.data.db.PlaylistEntity
import com.example.playlistmaker.library.data.db.TracklistDao
import com.example.playlistmaker.library.data.db.TracklistEntity
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.domain.models.SaveCoverResult
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.io.File

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val tracklistDao: TracklistDao,
    private val converter: PlaylistDBConverter,
    private val gson: Gson,
    private val context: Context
): PlaylistRepository {

    override fun getPlaylists(): Flow<List<Playlist>> =
        playlistDao.getPlaylists().map(::convertFromDB)

    override fun getPlaylist(id: Int): Flow<Playlist> = flow{
        emit(playlistDao.getPlaylist(id))
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

    override suspend fun setTracks(id: Int, tracks: List<String>, count: Int, track: Track) {
        playlistDao.updatePlTracks(id, gson.toJson(tracks), count)
        tracklistDao.insertTrackToList(convertTracklistToDB(track))
    }

    override suspend fun setTrackCount(id: Int, trackCountMethod: Int) {
        playlistDao.updatePlTrackCount(id, trackCountMethod)
    }

    override suspend fun saveImageToStorage(image: String): Flow<SaveCoverResult> = flow {
        val uri = Uri.parse(image)
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        inputStream.use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }

        val result = SaveCoverResult(absolutePath = file.absolutePath, uri = Uri.fromFile(file))

        emit(result)
    }
        .flowOn(Dispatchers.IO)

    private fun convertFromDB(entity: List<PlaylistEntity>) = entity.map(converter::map)

    private fun convertTracklistToDB(track: Track): TracklistEntity = converter.mapToTracklistEntity(track)

    private fun plToDB(playlist: Playlist): PlaylistEntity {
        return converter.map(playlist)
    }
}