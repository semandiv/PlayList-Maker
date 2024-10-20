package com.example.playlistmaker.player.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlayerRepository
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val playerDataModule = module {
    // Singleton MediaPlayer
    single { MediaPlayer() }

    factory { (previewUrl: String?) -> PlayerRepository(mediaPlayer = get(), previewUrl = previewUrl) }

    factory<PlayerInteractor> { (previewUrl: String?) ->
        PlayerInteractorImpl(playerRepository = get { parametersOf(previewUrl) })
    }

}