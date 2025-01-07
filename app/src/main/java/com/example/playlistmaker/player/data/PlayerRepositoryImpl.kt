package com.example.playlistmaker.player.data

import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val SAVED_TRACK = "saved_track_to_play"

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private val sharedPref: SharedPreferences,
    private val gson: Gson
): PlayerRepository {

    private var track: Track? = null
    private val previewUrl = track?.previewUrl ?: String()
    private var listener: (PlayerState) -> Unit = {}
    private var playerState: PlayerState = PlayerState.DEFAULT

    init {
        CoroutineScope(Dispatchers.IO).launch {
            track = getTrack()
        }
    }

    override fun setPlayerStateListener(listener: (PlayerState) -> Unit) {
        this.listener = listener
    }

    override fun sendTrack(): Track? = track

    override fun preparePlayer() {
        if (previewUrl.isNotEmpty()) {
            mediaPlayer.reset()
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

    override fun play() {
        playerState = PlayerState.PLAYING
        updatePlayerState(playerState)
        mediaPlayer.start()
    }

    override fun pause() {
        playerState = PlayerState.PAUSED
        updatePlayerState(playerState)
        mediaPlayer.pause()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun updatePlayer(){
        if (isPlaying()) {
            playerState = PlayerState.PLAYING
        } else {
            playerState = PlayerState.PAUSED
        }
        updatePlayerState(playerState)
    }

    private fun updatePlayerState(state: PlayerState) {
        listener.invoke(state)
    }

    private fun getTrack(): Track? {
        return sharedPref.getString(SAVED_TRACK, null)
            ?.let { jsonString ->
                gson.fromJson(jsonString, Track::class.java) }
    }
}