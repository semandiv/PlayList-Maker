package com.example.playlistmaker.search.di

import android.content.Context
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.TrackMapper
import com.example.playlistmaker.search.data.SharedPrefRepositoryImpl
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.AppleAPI
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.RetrofitProvider
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.SharedPrefRepository
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchDataModule = module {
    //RetrofitProvider
    single {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AppleAPI::class.java)
    }

    factory { Gson() }

    single { RetrofitProvider(get()) }

    //RetrofitNetworkClient
    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    //TrackRepository
    single { TrackMapper() }

    single<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    //SharedPrefRepository
    single {
        androidContext().getSharedPreferences("SEARCH_HISTORY_KEY", Context.MODE_PRIVATE)
    }

    single<SharedPrefRepository> { SharedPrefRepositoryImpl(get(), get()) }
}