package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.PlayerStateListener
import com.example.playlistmaker.domain.models.PlayerState

class PlayerRepository(private val mediaPlayer: MediaPlayer, private val previewUrl: String?) {
    private var listener: PlayerStateListener? = null
    private var playerState : PlayerState = PlayerState.DEFAULT

    private fun updatePlayerState(state: PlayerState){
        listener?.onPlayerStateChanged(state)
    }

    fun setPlayerStateListener(listener: PlayerStateListener) {
        this.listener = listener
    }

    fun preparePlayer(){
        if (previewUrl?.isNotEmpty() == true) {
            mediaPlayer.setDataSource(previewUrl)
            mediaPlayer.prepareAsync()
            playerState = PlayerState.PREPARED
            updatePlayerState(playerState)

            mediaPlayer.setOnPreparedListener {
                playerState = PlayerState.PREPARED
                updatePlayerState(playerState)
            }
            mediaPlayer.setOnCompletionListener {
                playerState = PlayerState.PREPARED
                updatePlayerState(playerState)
            }
        }
    }

    fun play() {
        playerState = PlayerState.PLAYING
        updatePlayerState(playerState)
        mediaPlayer.start()
    }

    fun pause(){
        playerState = PlayerState.PAUSED
        updatePlayerState(playerState)
        mediaPlayer.pause()
    }

    fun release() {
        mediaPlayer.release()
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition.toInt()
    }
}