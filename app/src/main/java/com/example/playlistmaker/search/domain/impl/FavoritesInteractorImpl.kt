package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.FavoritesInteractor
import com.example.playlistmaker.search.domain.db.FavoritesRespository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRespository
): FavoritesInteractor {

    override fun getFavoritesTrack(): Flow<List<Track>> {
        return favoritesRepository.getFavoritesTrack()
    }

    override fun getTracksID(): Flow<List<String>> {
        return favoritesRepository.getTracksID()
    }

    override suspend fun addTrack(track: Track) {
        favoritesRepository.addTrack(track)
    }

    override suspend fun removeTrack(track: Track) {
        favoritesRepository.removeTrack(track)
    }
}