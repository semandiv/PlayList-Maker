package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackRequest
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTracks(query: String): Pair<Int, List<Track>> {
        val response = networkClient.doRequest(TrackRequest(query))
        when (response.resultCode) {
            200 -> {
                return Pair(response.resultCode,(response as TracksResponse).results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.previewUrl,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.collectionName
                    )
                })
            }

            400 -> {
                return Pair(response.resultCode, emptyList())
            }

            else -> {
                return Pair(response.resultCode, emptyList())
            }
        }
    }
}