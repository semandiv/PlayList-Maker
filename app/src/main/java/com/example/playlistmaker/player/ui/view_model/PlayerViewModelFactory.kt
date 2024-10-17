package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.player.domain.api.PlayerInteractor

class PlayerViewModelFactory(
    private val previewUrl: String,
    private val playerInteractor: PlayerInteractor
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(previewUrl, playerInteractor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}