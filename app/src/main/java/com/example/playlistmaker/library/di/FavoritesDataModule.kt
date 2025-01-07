package com.example.playlistmaker.library.di

import androidx.room.Room
import com.example.playlistmaker.library.data.convertors.PlaylistDBConverter
import com.example.playlistmaker.library.data.convertors.TrackDBConvertor
import com.example.playlistmaker.library.data.db.PlaylistDatabase
import com.example.playlistmaker.library.data.db.TrackDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val favoritesDataModule = module {

    single {
        Room.databaseBuilder(androidContext(), TrackDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        Room.databaseBuilder(androidContext(), PlaylistDatabase::class.java, "pl_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }



    factory { TrackDBConvertor() }

    factory { PlaylistDBConverter()}

    // Получаем TrackDao из базы данных
    factory { get<TrackDatabase>().trackDao() }

    factory {get<PlaylistDatabase>().playlistDao()}

}