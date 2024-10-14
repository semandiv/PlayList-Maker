package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.data.network.NetworkResult
import com.example.playlistmaker.search.domain.models.Track

interface TracksInteractor {
    fun searchTracks(query: String, consumer: TrackConsumer)

    fun interface TrackConsumer {
        fun consume(foundTracks: NetworkResult<List<Track>>)
    }
}