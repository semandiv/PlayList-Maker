package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AppleAPI {
    @GET("/search?entity=song")
    fun searchTrack(@Query("term") text: String): Call<TracksResponse>
}