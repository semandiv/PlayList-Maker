package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.TrackSearchResult

interface TracksInteractor {
    fun searchTracks(query: String, consumer: TrackConsumer)

    fun interface TrackConsumer {
        fun consume(foundTracks: TrackSearchResult)
    }
}