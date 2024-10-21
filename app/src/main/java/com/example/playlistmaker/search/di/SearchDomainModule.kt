package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.SharedPrefRepository
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

private const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY_KEY"

val searchDomainModule = module {
    //TracksInteractor
    single<TracksInteractor> { TracksInteractorImpl(get()) }

    //HistoryInteractor
    single<HistoryInteractor> {
        val sharedPrefRepository: SharedPrefRepository = get { parametersOf(SEARCH_HISTORY_KEY) }
        HistoryInteractorImpl(sharedPrefRepository)
    }
}