package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.FavoritesInteractor
import com.example.playlistmaker.search.domain.api.ToPlayerInteractor
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favInteractor: FavoritesInteractor,
    private val toPlayerInteractor: ToPlayerInteractor
): ViewModel() {

    init {
        viewModelScope.launch {
            favInteractor.getFavoritesTrack()
                .collect{tracks ->
                    _tracks.value = tracks
                }
        }
    }

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks: StateFlow<List<Track>> get() = _tracks

    fun addTrack(track: Track) {
        viewModelScope.launch {
            favInteractor.addTrack(track)
        }
    }

    fun playTrack(track: Track) {
        toPlayerInteractor.toPlayer(track)
    }

    fun getTracks() {
        viewModelScope.launch {
            favInteractor.getFavoritesTrack()
                .collect{tracks ->
                    _tracks.value = tracks
                }
        }
    }
}