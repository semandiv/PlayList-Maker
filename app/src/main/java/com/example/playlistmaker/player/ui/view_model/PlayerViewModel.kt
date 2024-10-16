package com.example.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerState

class PlayerViewModel(
    private val previewUrl: String,
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private companion object {
        const val DELAY = 300L
    }

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> get() = _playerState

    private val _currentPosition = MutableLiveData<Int>()
    val currentPosition: LiveData<Int> get() = _currentPosition

    private val _isPrepared = MutableLiveData<Boolean>()
    val isPrepared: LiveData<Boolean> get() = _isPrepared


    private val handler = Handler(Looper.getMainLooper())

    init {
        playerInteractor.observePlayerState { state ->
            _playerState.postValue(state)
        }
        preparePlayer()
    }

    override fun onCleared() {
        releasePlayer()
        stopUpdateTime()
    }

    private fun preparePlayer() {
        _isPrepared.postValue(false)
        playerInteractor.preparePlayer()
        _isPrepared.postValue(true)
    }

    private fun startUpdatingTime() {
        handler.post(object : Runnable {
            override fun run() {
                if (playerInteractor.isPlaying()) {
                    _currentPosition.postValue(playerInteractor.currentPosition())
                    handler.postDelayed(this, DELAY)
                } else {
                    stopUpdateTime()
                }
            }
        })
    }

    private fun stopUpdateTime() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun getCurrentPosition() {
        if (_playerState.value == PlayerState.PLAYING) {
            _currentPosition.postValue(playerInteractor.currentPosition())
        } else {
            _currentPosition.postValue(0)
        }
    }

    fun play() {
        if (previewUrl.isNotEmpty()) {
            startUpdatingTime()
            playerInteractor.play()
        }
    }

    fun pause() {
        if (previewUrl.isNotEmpty()) {
            stopUpdateTime()
            playerInteractor.pause()
        }
    }

    fun releasePlayer() {
        if (previewUrl.isNotEmpty()) {
            playerInteractor.releasePlayer()
        }
    }

}