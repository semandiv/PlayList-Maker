package com.example.playlistmaker.library.domain.db

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun getFavoritesTrack(): Flow<List<Track>>

    suspend fun addTrack(track: Track)

    suspend fun removeTrack(track: Track)

    fun getTracksID(): Flow<List<String>>
}