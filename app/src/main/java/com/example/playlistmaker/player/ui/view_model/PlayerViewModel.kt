package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerState

class PlayerViewModel(
    private val previewUrl: String,
    private val playerInteractor: PlayerInteractor
): ViewModel() {
    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> get() = _playerState

    private val _currentPosition = MutableLiveData<Int>()
    val currentPosition: LiveData<Int> get() = _currentPosition

    init {
        playerInteractor.observePlayerState { state ->
            _playerState.postValue(state)
        }
        preparePlayer()
    }

    override fun onCleared() {
        releasePlayer()
    }

    private fun preparePlayer() {
        playerInteractor.preparePlayer()
    }

    fun getCurrentPosition(){
        if (previewUrl.isNotEmpty()) {
            _currentPosition.postValue(playerInteractor.currentPosition())
        } else {
            _currentPosition.postValue(0)
        }
    }

    fun play() {
        if (previewUrl.isNotEmpty()) {
            playerInteractor.play()
        }
    }

    fun pause() {
        if (previewUrl.isNotEmpty()) {
            playerInteractor.pause()
        }
    }

    fun releasePlayer() {
        if (previewUrl.isNotEmpty()) {
            playerInteractor.releasePlayer()
        }
    }

}