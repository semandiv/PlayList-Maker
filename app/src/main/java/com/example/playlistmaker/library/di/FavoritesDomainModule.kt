package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.data.FavoritesRepositoryImpl
import com.example.playlistmaker.library.domain.api.FavoritesInteractor
import com.example.playlistmaker.library.domain.db.FavoritesRepository
import com.example.playlistmaker.library.domain.impl.FavoritesInteractorImpl
import org.koin.dsl.module

val favoritesDomainModule = module {

    factory<FavoritesRepository> {
        FavoritesRepositoryImpl(
            trackDAO = get(),
            trackDBConvertor = get()
        )
    }

    factory<FavoritesInteractor>{
        FavoritesInteractorImpl(get())
    }
}