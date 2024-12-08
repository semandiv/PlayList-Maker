package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.TrackSearchResult
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(query: String): Flow<TrackSearchResult>
}