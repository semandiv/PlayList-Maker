package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RetrofitNetworkClient(private val appleAPI: AppleAPI) : NetworkClient {

    override suspend fun doRequest(dto: Any): NetworkResult {
        if (dto !is TrackRequest) return NetworkResult.Error

        return withContext(Dispatchers.IO) {
            try {
                val response = appleAPI.searchTrack(dto.expression)
                if (response.results.isNotEmpty()) {
                    NetworkResult.Success(response.results)
                } else {
                    NetworkResult.EmptyResult
                }
            } catch (e: IOException) {
                // Ошибка сети
                NetworkResult.NetworkException
            } catch (e: HttpException) {
                // Ошибка HTTP
                NetworkResult.Error
            }
        }
    }
}