package com.example.playlistmaker

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.PlayerRepository
import com.example.playlistmaker.data.SharedPrefRepositoryImpl
import com.example.playlistmaker.data.TrackMapper
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.RetrofitProvider
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.SharedPrefRepository
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.presenter.GetTrackImpl
import com.example.playlistmaker.presenter.api.GetTrack
import com.google.gson.Gson

object Creator {

    private val retrofitProvider = RetrofitProvider()

    private fun getTracksRepository(): TrackRepository {
        val appleAPI = retrofitProvider.provideAppleAPI()
        return TrackRepositoryImpl(RetrofitNetworkClient(appleAPI), TrackMapper())
    }

    private fun getPlayerRepository(previewUrl: String): PlayerRepository {
        return PlayerRepository(MediaPlayer(), previewUrl)
    }

    private fun getSharedPrefRepositiry(context: Context):SharedPrefRepository{
        return SharedPrefRepositoryImpl( context.getSharedPreferences(
            "SEARCH_HISTORY_KEY",
            Context.MODE_PRIVATE), gson = Gson()
        )
    }
    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(getSharedPrefRepositiry(context))
    }

    fun providePlayerInteractor(previewUrl: String): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository(previewUrl))
    }

    fun provideGetTrack(): GetTrack {
        return GetTrackImpl(provideTracksInteractor())
    }
}