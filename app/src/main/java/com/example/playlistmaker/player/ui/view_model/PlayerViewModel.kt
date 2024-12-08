package com.example.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.MediaState
import com.example.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private companion object {
        const val DELAY = 300L
        const val TIME_FORMAT = "mm:ss"
    }

    private val track: Track? = playerInteractor.loadTrack()
    private val previewUrl: String = track?.previewUrl ?: String()

    private val _currentPosition = MutableLiveData<String>()
    val currentPosition: LiveData<String> get() = _currentPosition

    private val _mediaState = MutableLiveData<MediaState>()
    val mediaState: LiveData<MediaState> get() = _mediaState

    val timePlay: String = SimpleDateFormat(
        TIME_FORMAT,
        Locale.getDefault()
    ).format(track?.trackTimeMillis?.toLong() ?: String())

    private val handler = Handler(Looper.getMainLooper())

    init {
        playerInteractor.observeMediaState (_mediaState::postValue)
        preparePlayer()
    }

    override fun onCleared() {
        releasePlayer()
        stopUpdateTime()
    }

    fun getCoverUrl() = track?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

    fun onClickPlayButton(){
        when(mediaState.value){
            MediaState.Default -> { /* NOP*/}
            MediaState.Paused -> play()
            MediaState.Playing -> pause()
            is MediaState.Prepared -> play()
            null -> { }
        }
    }

    fun getTrack(): Track? = track

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

    private fun preparePlayer() {
        playerInteractor.preparePlayer()
    }

    private fun play() {
        if (previewUrl.isNotEmpty()) {
            startUpdatingTime()
            playerInteractor.play()
        }
    }

    private fun startUpdatingTime() {
        handler.post(object : Runnable {
            override fun run() {
                if (playerInteractor.isPlaying()) {
                    _currentPosition.postValue(getPositionToString(playerInteractor.currentPosition()))
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

    private fun getPositionToString(position: Int): String {
        return SimpleDateFormat(
            TIME_FORMAT,
            Locale.getDefault()
        ).format(position)
    }
}