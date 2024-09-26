package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackRequest

class RetrofitNetworkClient(private val appleAPI: AppleAPI) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        return if (dto is TrackRequest) {
            try {
                val resp = appleAPI.searchTrack(dto.expression).execute()
                if (resp.isSuccessful) {
                    resp.body()?.apply{ resultCode = resp.code()}?: Response(400)
                } else {
                    throw Exception("Ошибка связи с Apple: ${resp.code()}")
                }
            } catch (e: Exception) {
                Response(400)
            }

        } else {
            Response(400)
        }
    }
}