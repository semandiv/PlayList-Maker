package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackRequest

class RetrofitNetworkClient(private val appleAPI: AppleAPI) : NetworkClient {

    override fun <T> doRequest(dto: Any, responseType: Class<T>): NetworkResult<List<T>> {
        return if (dto is TrackRequest) {
            try {
                val response = appleAPI.searchTrack(dto.expression).execute()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val data = body.results as? List<T>
                        if (data != null) {
                            NetworkResult.Success(data)
                        } else {
                            NetworkResult.Error(400, "Ошибка преобразования данных")
                        }
                    } else {
                        NetworkResult.Error(response.code(), "Треки не найдены")
                    }
                } else {
                    NetworkResult.Error(response.code(), "Ошибка связи с сервером")
                }

            } catch (e: Exception) {
                NetworkResult.NetworkException(e)
            }

        } else {
            NetworkResult.Error(400, "Неизвестная ошибка")
        }
    }
}