package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackRequest

class RetrofitNetworkClient(private val appleAPI: AppleAPI) : NetworkClient {

    override fun doRequest(dto: Any): NetworkResult {
        if (dto !is TrackRequest) return NetworkResult.NetworkException

        return try {
            val response = appleAPI.searchTrack(dto.expression).execute()
            val data = response.body()?.results

            when {
                response.isSuccessful && data != null->
                    if (data.isNotEmpty()) NetworkResult.Success(data) else NetworkResult.EmptyResult
                else -> NetworkResult.Error
            }
        } catch (e: Exception) {
            NetworkResult.NetworkException
        }
    }
}