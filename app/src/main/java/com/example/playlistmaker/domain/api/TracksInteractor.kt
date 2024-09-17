package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(query: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: Pair<Int, List<Track>>)
    }
}