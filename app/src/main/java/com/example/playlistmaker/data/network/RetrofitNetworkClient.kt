package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val appleBaseURL = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(appleBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val appleAPI = retrofit.create(AppleAPI::class.java)

    override fun doRequest(dto: Any): Response {
        var newResponse: Response = Response()
        if (dto is TrackRequest) {
            try {
                val resp = appleAPI.searchTrack(dto.expression).execute()
                if (resp.isSuccessful) {
                    newResponse = resp.body() ?: Response()
                    newResponse.apply { resultCode = resp.code() }
                } else {
                    throw Exception("Ошибка связи с Apple: ${resp.code()}")
                }
            } catch (e: Exception) {
                newResponse = Response().apply { resultCode = 400 }
            }

        }
        return newResponse
    }
}