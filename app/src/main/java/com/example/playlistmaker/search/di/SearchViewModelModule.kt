package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {

    viewModel { SearchViewModel(tracksInteractor = get(), historyInteractor = get(), toPlayerInteractor = get()) }
}