package com.example.playlistmaker.player.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlayerRepository
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val playerDataModule = module {
    factory { MediaPlayer() }

    factory { (previewUrl: String?) -> PlayerRepository(mediaPlayer = get(), previewUrl = previewUrl) }

    factory<PlayerInteractor> { (previewUrl: String?) ->
        PlayerInteractorImpl(playerRepository = get { parametersOf(previewUrl) })
    }

}