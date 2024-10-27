package com.example.playlistmaker.player.di

import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlayerRepository
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val playerDataModule = module {
    factory { MediaPlayer() }

    factory {
        val sharedPref: SharedPreferences = get() { parametersOf("track_to_play") }
        PlayerRepository(mediaPlayer = get(), sharedPref = sharedPref, gson = get())
    }

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }
}