package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.TrackSearchResult

interface TracksInteractor {
    fun searchTracks(query: String, consumer: TrackConsumer)

    fun interface TrackConsumer {
        fun consume(foundTracks: TrackSearchResult)
    }
}