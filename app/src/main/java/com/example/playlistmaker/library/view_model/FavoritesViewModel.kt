package com.example.playlistmaker.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.FavoritesInteractor
import com.example.playlistmaker.search.domain.api.ToPlayerInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favInteractor: FavoritesInteractor,
    private val toPlayerInteractor: ToPlayerInteractor
): ViewModel() {

    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> get() = _tracks

    private val _tracksID = MutableLiveData<List<String>>()
    val tracksID: LiveData<List<String>> get() = _tracksID

    fun getTracks() {
        viewModelScope.launch {
            favInteractor.getFavoritesTrack()
                .collect{tracks ->
                    _tracks.value = tracks
                }
        }

    }

    fun getTracksID() {
        viewModelScope.launch {
            favInteractor.getTracksID()
                .collect {
                    _tracksID.value = it
                }
        }
    }

    fun addTrack(track: Track) {
        viewModelScope.launch {
            favInteractor.addTrack(track)
        }
    }

    fun removeTrack(track: Track) {
        viewModelScope.launch {
            favInteractor.removeTrack(track)
        }
    }

    fun playTrack(track: Track) {
        toPlayerInteractor.toPlayer(track)
    }
}