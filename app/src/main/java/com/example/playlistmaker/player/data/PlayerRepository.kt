package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.models.PlayerState

class PlayerRepository(private val mediaPlayer: MediaPlayer, private val previewUrl: String?) {
    private var listener: (PlayerState) -> Unit = {}
    private var playerState : PlayerState = PlayerState.DEFAULT

    private fun updatePlayerState(state: PlayerState){
        listener.invoke(state)
    }

    fun setPlayerStateListener(listener: (PlayerState) -> Unit) {
        this.listener = listener
    }

    fun preparePlayer(){
        if (!previewUrl.isNullOrEmpty()) {
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
        return mediaPlayer.currentPosition
    }

    fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }
}