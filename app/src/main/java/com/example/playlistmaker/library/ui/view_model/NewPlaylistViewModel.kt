package com.example.playlistmaker.library.ui.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.api.PlaylistInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val plInteractor: PlaylistInteractor
) : ViewModel() {

    private companion object{
        const val PL_ID = 0
    }

    var plNameVM = String()
    var plDescriptionVM = String()
    var savedImageUri: Uri = Uri.EMPTY
    private var imagePath = String()


    fun createPlaylist() {
        viewModelScope.launch {
            val playlist = Playlist(PL_ID, plNameVM, plDescriptionVM, imagePath)
            plInteractor.createPlaylist(playlist)
        }
    }

    fun saveImage(image: String, callback: (Uri) -> Unit) {
        viewModelScope.launch {
            plInteractor.saveImageToStorage(image)
                .collect {
                    val result = it.split(";", limit = 2)
                    imagePath = result[0]
                    val imageUri = Uri.parse(result[1])
                    callback(imageUri)
                }
        }
    }
}