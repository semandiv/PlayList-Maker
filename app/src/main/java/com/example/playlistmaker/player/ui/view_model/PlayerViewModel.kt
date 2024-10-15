package com.example.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerState
import kotlinx.coroutines.Delay

class PlayerViewModel(
    private val previewUrl: String,
    private val playerInteractor: PlayerInteractor
): ViewModel() {

    private companion object{
        const val DELAY = 300L
    }

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> get() = _playerState

    private val _currentPosition = MutableLiveData<Int>()
    val currentPosition: LiveData<Int> get() = _currentPosition

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> get() = _isPlaying

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
        playerInteractor.preparePlayer()
        _isPlaying.postValue(false)
        _isPrepared.postValue(true)
    }

    private fun startUpdatingTime(): Runnable{
        return object : Runnable {
            override fun run() {
                when(_playerState.value){
                    PlayerState.DEFAULT -> handler.removeCallbacks(this)
                    PlayerState.PREPARED -> {
                        _isPrepared.postValue(true)
                        //handler.postDelayed(this, DELAY)
                        handler.removeCallbacks(this)
                    }
                    PlayerState.PLAYING -> {
                        _isPrepared.postValue(false)
                        getCurrentPosition()
                        handler.postDelayed(this, DELAY)
                        //handler.removeCallbacks(this)
                    }
                    PlayerState.PAUSED -> handler.removeCallbacks(this)
                    null -> handler.removeCallbacks(this)
                }
            }
        }
    }

    private fun stopUpdateTime() {
        handler.removeCallbacks(startUpdatingTime())
    }

    private fun startUpdateTime() {
        handler.post(startUpdatingTime())
    }

    private fun getCurrentPosition(){
        if (previewUrl.isNotEmpty()) {
            _currentPosition.postValue(playerInteractor.currentPosition())
        } else {
            _currentPosition.postValue(0)
        }
    }

    fun play() {
        if (previewUrl.isNotEmpty()) {
            startUpdateTime()
            playerInteractor.play()
            _isPlaying.postValue(true)
            _isPrepared.postValue(false)
        }
    }

    fun pause() {
        if (previewUrl.isNotEmpty()) {
            stopUpdateTime()
            playerInteractor.pause()
            _isPlaying.postValue(false)
        }
    }

    fun releasePlayer() {
        if (previewUrl.isNotEmpty()) {
            playerInteractor.releasePlayer()
        }
    }

}