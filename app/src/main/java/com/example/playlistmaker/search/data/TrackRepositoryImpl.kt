package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackRequest
import com.example.playlistmaker.search.data.network.NetworkResult
import com.example.playlistmaker.search.domain.api.TrackMapper
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.models.TrackSearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val trackMapper: TrackMapper
) : TrackRepository {

    override fun searchTracks(query: String): Flow<TrackSearchResult> = flow {
        when (val response = networkClient.doRequest(TrackRequest(query))) {
            NetworkResult.EmptyResult -> emit(TrackSearchResult.NoResult)
            NetworkResult.Error, NetworkResult.NetworkException -> emit(TrackSearchResult.NetworkError)
            is NetworkResult.Success -> {
                val tracks = response.data.map { trackMapper.map(it) }
                if (tracks.isNotEmpty()) emit(TrackSearchResult.SearchResult(tracks))
                else emit(TrackSearchResult.NoResult)
            }
        }
    }
}