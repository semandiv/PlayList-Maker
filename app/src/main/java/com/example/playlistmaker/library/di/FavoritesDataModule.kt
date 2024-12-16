package com.example.playlistmaker.library.di

import androidx.room.Room
import com.example.playlistmaker.library.data.convertors.TrackDBConvertor
import com.example.playlistmaker.library.data.db.TrackDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val favoritesDataModule = module {
    single {
        Room.databaseBuilder(androidContext(), TrackDatabase::class.java, "database.db")
            .build()
    }

    factory { TrackDBConvertor() }
}