package com.example.playlistmaker.library.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val plInteractor: PlaylistInteractor
) : ViewModel() {

    fun createPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            plInteractor.createPlaylist(playlist)
        }
    }
}