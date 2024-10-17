package com.example.playlistmaker.creator

import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.player.data.PlayerRepository
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import com.example.playlistmaker.player.ui.view_model.PlayerViewModelFactory
import com.example.playlistmaker.search.data.SharedPrefRepositoryImpl
import com.example.playlistmaker.search.data.TrackMapper
import com.example.playlistmaker.search.data.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.RetrofitProvider
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.SharedPrefRepository
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.search.ui.view_model.SearchViewModelFactory
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModelFactory
import com.google.gson.Gson

const val THEME_SWITCHER = "theme_checker"

object Creator {

    private val retrofitProvider = RetrofitProvider()

    private fun getTracksRepository(): TrackRepository {
        val appleAPI = retrofitProvider.provideAppleAPI()
        return TrackRepositoryImpl(RetrofitNetworkClient(appleAPI), TrackMapper())
    }

    private fun getPlayerRepository(previewUrl: String): PlayerRepository {
        return PlayerRepository(MediaPlayer(), previewUrl)
    }

    private fun getSharedPrefRepositiry(context: Context): SharedPrefRepository {
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

    fun provideSearchViewModel(activity: AppCompatActivity): SearchViewModel {
        val searchFactory = SearchViewModelFactory(provideTracksInteractor(),
            provideHistoryInteractor(activity))
        return ViewModelProvider(activity, searchFactory).get(SearchViewModel::class.java)
    }

    fun providePlayerViewModel(activity: AppCompatActivity, previewUrl: String): PlayerViewModel {
        val playerFactory = PlayerViewModelFactory(previewUrl,providePlayerInteractor(previewUrl))
        return ViewModelProvider(activity, playerFactory).get(PlayerViewModel::class.java)
    }

    fun provideSettingsViewModel(activity: AppCompatActivity): SettingsViewModel {
        val sharedPrefs = activity.getSharedPreferences(THEME_SWITCHER, Context.MODE_PRIVATE)
        val factory = SettingsViewModelFactory(sharedPrefs)
        return ViewModelProvider(activity, factory).get(SettingsViewModel::class.java)
    }
}