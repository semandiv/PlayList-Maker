package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.data.network.NetworkResult
import com.example.playlistmaker.search.domain.api.TrackMapper
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.TrackSearchResult
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val trackRepository: TrackRepository,
    private val trackMapper: TrackMapper
) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(query: String, consumer: TracksInteractor.TrackConsumer) {
        executor.execute {
            val result = when (val response = trackRepository.searchTracks(query)) {
                is NetworkResult.Success -> {
                    val tracks = response.data.map { trackMapper.map(it) }
                    if (tracks.isNotEmpty()) TrackSearchResult.SearchResult(tracks)
                    else TrackSearchResult.NoResult
                }
                is NetworkResult.EmptyResult -> TrackSearchResult.NoResult
                is NetworkResult.Error, is NetworkResult.NetworkException -> TrackSearchResult.NetworkError
            }
            consumer.consume(result)
        }
    }
}