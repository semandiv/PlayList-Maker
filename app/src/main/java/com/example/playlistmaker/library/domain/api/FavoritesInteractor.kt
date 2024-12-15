package com.example.playlistmaker.library.domain.api

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun getFavoritesTrack(): Flow<List<Track>>
    fun getTracksID(): Flow<List<String>>
    suspend fun addTrack(track: Track)
    suspend fun removeTrack(track: Track)
}