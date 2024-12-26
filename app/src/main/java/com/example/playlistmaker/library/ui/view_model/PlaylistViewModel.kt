package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val plInteractor: PlaylistInteractor
) : ViewModel() {

    init {
        getPlaylists()
    }

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> get() = _playlists

    fun getPlaylists() {
        viewModelScope.launch {
            plInteractor.getAllPlaylists()
                .collect { playlists ->
                    _playlists.postValue(playlists)
                }
        }
    }

    fun newPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            plInteractor.createPlaylist(playlist)
        }
    }

}