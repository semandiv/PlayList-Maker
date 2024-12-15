package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.data.FavoritesRepositoryImpl
import com.example.playlistmaker.library.domain.api.FavoritesInteractor
import com.example.playlistmaker.library.domain.db.FavoritesRespository
import com.example.playlistmaker.library.domain.impl.FavoritesInteractorImpl
import org.koin.dsl.module

val favoritesDomainModule = module {

    single<FavoritesRespository>{
        FavoritesRepositoryImpl(get(), get())
    }

    single<FavoritesInteractor>{
        FavoritesInteractorImpl(get())
    }
}