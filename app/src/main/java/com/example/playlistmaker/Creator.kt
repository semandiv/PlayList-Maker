package com.example.playlistmaker

import android.media.MediaPlayer
import com.example.playlistmaker.data.PlayerRepository
import com.example.playlistmaker.data.SharedPrefRepositoryImpl
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.SharedPrefRepository
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSharedPrefRepositiry():SharedPrefRepository{
        return SharedPrefRepositoryImpl(App.sharedPreferences)
    }

    fun provideHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(getSharedPrefRepositiry())
    }

    private fun getPlayerRepository(previewUrl: String): PlayerRepository {
        return PlayerRepository(MediaPlayer(), previewUrl)
    }

    fun providePlayerInteractor(previewUrl: String): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository(previewUrl))
    }
}