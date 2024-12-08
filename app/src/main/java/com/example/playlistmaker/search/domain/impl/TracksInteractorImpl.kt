package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.TrackSearchResult
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(private val trackRepository: TrackRepository) : TracksInteractor {

    override fun searchTracks(query: String): Flow<TrackSearchResult> {
        return trackRepository.searchTracks(query)
    }
}