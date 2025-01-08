package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val plInteractor: PlaylistInteractor
) : ViewModel() {

    init {
        getPlaylists()

        viewModelScope.launch {
            _selectedPlaylistID.collect { playlistID ->
                val playlist = plInteractor.getPlaylist(playlistID).firstOrNull()
                _selectedPlaylist.update { playlist }
            }
        }
    }

    private val _selectedPlaylistID = MutableStateFlow(0)

    private val _selectedPlaylist = MutableStateFlow<Playlist?>(null)
    val selectedPlaylist: StateFlow<Playlist?> = _selectedPlaylist

    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> get() = _playlists

    private fun getPlaylists() {
        viewModelScope.launch {
            plInteractor.getAllPlaylists()
                .collect { playlists ->
                    _playlists.update { playlists }
                }
        }
    }

    fun openPlaylist(playlistID: Int) {
        _selectedPlaylistID.update { playlistID }
    }
}