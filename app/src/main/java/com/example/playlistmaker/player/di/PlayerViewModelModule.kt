package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.ui.view_model.PlayerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val playerViewModelModule = module {

    viewModel {
        PlayerViewModel(
            playerInteractor = get(),
            favoritesInteractor = get(),
            playlistInteractor = get(),
            gson = get())
    }
}