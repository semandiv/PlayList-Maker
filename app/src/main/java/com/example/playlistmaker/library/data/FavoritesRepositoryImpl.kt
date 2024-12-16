package com.example.playlistmaker.library.data

import com.example.playlistmaker.library.data.convertors.TrackDBConvertor
import com.example.playlistmaker.library.data.db.TrackDatabase
import com.example.playlistmaker.library.data.db.TrackEntity
import com.example.playlistmaker.library.domain.db.FavoritesRespository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val trackDatabase: TrackDatabase,
    private val trackDBConvertor: TrackDBConvertor
): FavoritesRespository {

    override fun getFavoritesTrack(): Flow<List<Track>> = trackDatabase.trackDao().getAllTracks()
        .map { trackEntities -> convertFromDB(trackEntities) }

    override suspend fun addTrack(track: Track) {
        trackDatabase.trackDao().insertTrack(convertToDB(track))
    }

    override suspend fun removeTrack(track: Track) {
        trackDatabase.trackDao().deleteTrack(convertToDB(track))
    }

    override fun getTracksID(): Flow<List<String>> = flow {
        val tracksID = trackDatabase.trackDao().getTrackId()
        emit(tracksID)
    }

    private fun convertFromDB(tracksDB: List<TrackEntity>): List<Track>{
        return tracksDB.map { track -> trackDBConvertor.map(track) }
    }

    private fun convertToDB(track: Track): TrackEntity {
        return trackDBConvertor.map(track)
    }

}