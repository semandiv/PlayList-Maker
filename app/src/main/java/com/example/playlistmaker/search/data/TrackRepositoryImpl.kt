package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackDTO
import com.example.playlistmaker.search.data.dto.TrackRequest
import com.example.playlistmaker.search.data.network.NetworkResult
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient, private val trackMapper: TrackMapper) :
    TrackRepository {
    override fun <T> searchTracks(query: String, responseType: Class<T>): NetworkResult<List<T>> {
        val response = networkClient.doRequest(TrackRequest(query), TrackDTO::class.java)
        return when (response) {
            is NetworkResult.Success-> {
                if(responseType.isAssignableFrom(Track::class.java)){
                    val tracks = trackMapper.map(response.data) as List<T>
                    NetworkResult.Success(tracks)
                } else{
                    NetworkResult.Error(400)
                }
            }
            is NetworkResult.Error -> {
                NetworkResult.Error(400)
            }
            is NetworkResult.NetworkException -> {
                NetworkResult.NetworkException(response.exception)
            }
        }
    }
}