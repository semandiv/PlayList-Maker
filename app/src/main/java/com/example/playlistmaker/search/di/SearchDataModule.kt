package com.example.playlistmaker.search.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.SharedPrefRepositoryImpl
import com.example.playlistmaker.search.data.TrackPlayRepositoryImpl
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.AppleAPI
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.SharedPrefRepository
import com.example.playlistmaker.search.domain.api.TrackMapper
import com.example.playlistmaker.search.domain.api.TrackPlayRepository
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.parameter.parametersOf
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
    factory {(name: String) ->
        androidContext().getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    factory<SharedPrefRepository> { (name: String) ->
        val sharedPrefs: SharedPreferences = get { parametersOf(name) }
        SharedPrefRepositoryImpl(sharedPrefs, get())
    }

    factory<TrackPlayRepository> { (name: String) ->
        val sharedPrefs: SharedPreferences = get { parametersOf(name) }
        TrackPlayRepositoryImpl(sharedPrefs, get())
    }


}