package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AppleAPI {
    @GET("/search?entity=song")
    fun searchTrack(@Query("term") text: String): Call<TracksResponse>
}