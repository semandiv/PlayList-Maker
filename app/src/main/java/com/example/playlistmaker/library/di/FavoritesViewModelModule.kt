package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.ui.view_model.FavoritesViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val favoritesViewModelModule = module {
    viewModel {
        FavoritesViewModel(get(),get())
    }
}