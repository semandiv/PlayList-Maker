package com.example.playlistmaker.library.ui.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val plInteractor: PlaylistInteractor
) : ViewModel() {

    private companion object{
        const val PLAYLIST_ID = 0
    }

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> get() = _imageUri

    private var imagePath = String()


    fun createPlaylist(plNameVM: String, plDescriptionVM: String) {
        viewModelScope.launch {
            val playlist = Playlist(PLAYLIST_ID, plNameVM, plDescriptionVM, imagePath)
            plInteractor.createPlaylist(playlist)
        }
    }

    fun saveImage(image: String) {
        viewModelScope.launch {
            plInteractor.saveImageToStorage(image)
                .collect {
                    imagePath = it.absolutePath
                    _imageUri.value = it.uri
                }
        }
    }
}