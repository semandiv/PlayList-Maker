package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.PlayerStateListener
import com.example.playlistmaker.domain.models.PlayerState

class PlayerRepository(private val mediaPlayer: MediaPlayer, previewUrl: String?) {
    private var listener: PlayerStateListener? = null

    init {
        preparePlayer(previewUrl)
    }


    private var playerState = PlayerState.DEFAULT

    fun setPlayerStateListener(listener: PlayerStateListener) {
        this.listener = listener
    }
    private fun preparePlayer(previewUrl: String?){
        if (previewUrl?.isNotEmpty() == true) {
            mediaPlayer.setDataSource(previewUrl)
            mediaPlayer.prepareAsync()

            mediaPlayer.setOnPreparedListener {
                listener?.onPlayerStateChanged(PlayerState.PREPARED)
            }
            mediaPlayer.setOnCompletionListener {
                listener?.onPlayerStateChanged(PlayerState.PREPARED)
            }
        }
    }

    fun play(): PlayerState {
        listener?.onPlayerStateChanged(PlayerState.PLAYING)
        mediaPlayer.start()
        return playerState
    }

    fun pause(): PlayerState{
        listener?.onPlayerStateChanged(PlayerState.PAUSED)
        mediaPlayer.pause()
        return playerState
    }

    fun release() {
        mediaPlayer.release()
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition.toInt()
    }
}