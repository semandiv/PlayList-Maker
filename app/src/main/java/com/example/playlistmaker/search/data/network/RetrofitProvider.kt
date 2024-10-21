package com.example.playlistmaker.search.data.network

import retrofit2.Retrofit

class RetrofitProvider (private val retrofit: Retrofit) {

    fun provideAppleAPI(): AppleAPI {
        return retrofit.create(AppleAPI::class.java)
    }
}