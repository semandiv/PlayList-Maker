package com.example.playlistmaker.search.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProvider (private val retrofit: Retrofit) {

    fun provideAppleAPI(): AppleAPI {
        return retrofit.create(AppleAPI::class.java)
    }
}