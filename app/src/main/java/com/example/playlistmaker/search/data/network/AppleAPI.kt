package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.TracksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AppleAPI {
    @GET("/search?entity=song")
    suspend fun searchTrack(@Query("term") text: String): TracksResponse
}