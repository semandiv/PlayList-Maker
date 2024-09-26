package com.example.playlistmaker.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProvider {

    private companion object{
        const val BASE_URL = "https://itunes.apple.com/"
    }

    // Метод для создания Retrofit
    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Метод для создания AppleAPI
    fun provideAppleAPI(): AppleAPI {
        return provideRetrofit().create(AppleAPI::class.java)
    }
}