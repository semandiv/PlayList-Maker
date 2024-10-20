package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import org.koin.dsl.module

val searchDomainModule = module {
    //TracksInteractor
    single<TracksInteractor> { TracksInteractorImpl(get()) }

    //HistoryInteractor
    single<HistoryInteractor>{ HistoryInteractorImpl(get()) }


}