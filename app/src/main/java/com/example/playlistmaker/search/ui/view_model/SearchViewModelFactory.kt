package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor

class SearchViewModelFactory(
    private val tracksInteractor: TracksInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(tracksInteractor, historyInteractor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
