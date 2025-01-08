package com.example.playlistmaker.library.data

import com.example.playlistmaker.library.data.convertors.TrackDBConvertor
import com.example.playlistmaker.library.data.db.TrackDao
import com.example.playlistmaker.library.data.db.TrackEntity
import com.example.playlistmaker.library.domain.db.FavoritesRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val trackDAO: TrackDao,
    private val trackDBConvertor: TrackDBConvertor
): FavoritesRepository {

    override fun getFavoritesTrack(): Flow<List<Track>> =
        trackDAO.getAllTracks().map(::convertFromDB)

    override suspend fun addTrack(track: Track) {
        track.timeStamp = System.currentTimeMillis()
        trackDAO.insertTrack(convertToDB(track))
    }

    override suspend fun removeTrack(track: Track) {
        trackDAO.deleteTrack(convertToDB(track))
    }

    override fun getTracksID(): Flow<List<String>> = flow {
        val tracksID = trackDAO.getTrackId()
        emit(tracksID)
    }

    private fun convertFromDB(tracksDB: List<TrackEntity>) = tracksDB.map(trackDBConvertor::map)

    private fun convertToDB(track: Track): TrackEntity {
        return trackDBConvertor.map(track)
    }

}