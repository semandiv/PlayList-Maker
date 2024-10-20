package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val playerViewModelModule = module {

    viewModel { (previewUrl: String) ->
        PlayerViewModel(
            previewUrl = previewUrl,
            playerInteractor = get { parametersOf(previewUrl) })
    }
}