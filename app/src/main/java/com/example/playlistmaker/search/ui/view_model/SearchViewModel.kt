package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.data.network.NetworkResult
import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.ToPlayerInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val historyInteractor: HistoryInteractor,
    private val toPlayerInteractor: ToPlayerInteractor
) : ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    private val _loadError = MutableLiveData<Unit>()
    val loadError: LiveData<Unit> get() = _loadError

    private val _networkError = MutableLiveData<Unit>()
    val networkError: LiveData<Unit> get() = _networkError

    private val _history = MutableLiveData<List<Track>>()
    val history: LiveData<List<Track>> get() = _history

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    init {
        loadHistory()
    }

    fun searchTracks(query: String) {
        _isLoading.value = true
        tracksInteractor.searchTracks(query, { result ->
            when (result) {
                is NetworkResult.Success -> {
                    _tracks.postValue(result.data)
                    _isLoading.postValue(false)
                }

                is NetworkResult.Error -> {
                    _loadError.postValue(Unit)
                    _isLoading.postValue(false)
                }

                is NetworkResult.NetworkException -> {
                    _loadError.postValue(Unit)
                    _isLoading.postValue(false)
                }
            }
        })
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
