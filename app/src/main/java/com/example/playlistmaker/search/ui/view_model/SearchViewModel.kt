package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.ToPlayerInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TrackSearchResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val historyInteractor: HistoryInteractor,
    private val toPlayerInteractor: ToPlayerInteractor
) : ViewModel() {

    private val _history = MutableStateFlow<List<Track>>(emptyList())
    val history: StateFlow<List<Track>> get() = _history

    private val _searchResult = MutableStateFlow<TrackSearchResult>(TrackSearchResult.NoResult)
    val searchResult: StateFlow<TrackSearchResult> get() = _searchResult

    init {
        loadHistory()
    }

    fun searchedTracks(query: String) {
        if(query.isNotEmpty()){
            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(query)
                    .collect { result ->
                        _searchResult.value = result
                    }
            }
        }
    }

    fun playTrack(track: Track) {
        toPlayerInteractor.toPlayer(track)
    }

    fun loadHistory() {
        _history.value = historyInteractor.getTrack().reversed()
    }

    fun saveTrackToHistory(track: Track) {
        historyInteractor.addTrack(track)
        loadHistory()
    }

    fun clearHistory() {
        historyInteractor.clearTrack()
        loadHistory()
    }
}
