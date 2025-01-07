package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.data.FavoritesRepositoryImpl
import com.example.playlistmaker.library.data.PlaylistRepositoryImpl
import com.example.playlistmaker.library.domain.api.FavoritesInteractor
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.domain.db.FavoritesRepository
import com.example.playlistmaker.library.domain.db.PlaylistRepository
import com.example.playlistmaker.library.domain.impl.FavoritesInteractorImpl
import com.example.playlistmaker.library.domain.impl.PlaylistInteractorImpl
import org.koin.android.ext.koin.androidContext
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

    factory<PlaylistRepository>{
        PlaylistRepositoryImpl(
            playlistDao = get(),
            converter = get(),
            gson = get(),
            androidContext()
        )
    }

    factory<PlaylistInteractor>{
        PlaylistInteractorImpl(get())
    }
}