package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import java.util.concurrent.Executors

class TracksInteractorImpl(private val trackRepository: TrackRepository) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(query: String, consumer: TracksInteractor.TrackConsumer) {
        executor.execute{
            val result = trackRepository.searchTracks(query, Track::class.java)
            consumer.consume(result)
        }
    }
}