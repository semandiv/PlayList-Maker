package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.ToPlayerInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TrackSearchResult

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val historyInteractor: HistoryInteractor,
    private val toPlayerInteractor: ToPlayerInteractor
) : ViewModel() {

    private val _history = MutableLiveData<List<Track>>()
    val history: LiveData<List<Track>> get() = _history

    private val _searchResult = MutableLiveData<TrackSearchResult>()
    val searchResult: LiveData<TrackSearchResult> get() = _searchResult

    fun searchedTracks(query: String) {
        tracksInteractor.searchTracks(query){ result ->
            _searchResult.postValue(result)
        }
    }

    init {
        loadHistory()
    }

    fun playTrack(track: Track) {
        toPlayerInteractor.toPlayer(track)
    }

    fun loadHistory() {
        _history.postValue(historyInteractor.getTrack().reversed())
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
