package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.convertors.TrackDBConvertor
import com.example.playlistmaker.search.data.db.TrackDatabase
import com.example.playlistmaker.search.data.db.TrackEntity
import com.example.playlistmaker.search.domain.db.FavoritesRespository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val trackDatabase: TrackDatabase,
    private val trackDBConvertor: TrackDBConvertor
): FavoritesRespository{

    override fun getFavoritesTrack(): Flow<List<Track>> = flow {
        val tracks = trackDatabase.trackDao().getAllTracks()
        emit(convertFromDB(tracks))
    }

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

    private fun convertToDB(track: Track): TrackEntity{
        return trackDBConvertor.map(track)
    }

}