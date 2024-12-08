package com.example.playlistmaker.player.data

import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson


private const val SAVED_TRACK = "saved_track_to_play"

class PlayerRepository(
    private val mediaPlayer: MediaPlayer,
    private val sharedPref: SharedPreferences,
    private val gson: Gson
) {

    private val track = getTrack()
    private val previewUrl = track?.previewUrl ?: String()
    private var listener: (PlayerState) -> Unit = {}
    private var playerState: PlayerState = PlayerState.DEFAULT

    private fun updatePlayerState(state: PlayerState) {
        listener.invoke(state)
    }

    fun setPlayerStateListener(listener: (PlayerState) -> Unit) {
        this.listener = listener
    }

    private fun getTrack(): Track? {
        return sharedPref.getString(SAVED_TRACK, null)
            ?.let { jsonString ->
                gson.fromJson(jsonString, Track::class.java) }
    }

    fun sendTrack(): Track? = track

    fun preparePlayer() {
        if (previewUrl.isNotEmpty()) {
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

    fun pause() {
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