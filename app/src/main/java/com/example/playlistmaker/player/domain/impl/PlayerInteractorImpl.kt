package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.data.PlayerRepository
import com.example.playlistmaker.player.domain.api.MediaStateListener
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.MediaState
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.search.domain.models.Track

class PlayerInteractorImpl (private val playerRepository: PlayerRepository) : PlayerInteractor, MediaStateListener {

    private var mediaState: MediaState = MediaState.Default
    private var mediaStateListener: MediaStateListener? = null
    private var stateListener: (MediaState) -> Unit = {}

    private fun setStateListener(listener: (MediaState) -> Unit) {
        stateListener = listener
    }

    private fun updateState(newState: MediaState) {
        stateListener.invoke(newState)
    }

    override fun observeMediaState(listener: (MediaState) -> Unit) {

        this.setStateListener(listener)
    }

    override fun observePlayerState(listener: (PlayerState) -> Unit) {
        playerRepository.setPlayerStateListener { newState->
            when (newState) {
                PlayerState.PREPARED -> updateState(MediaState.Prepared)
                PlayerState.PLAYING -> updateState(MediaState.Playing)
                PlayerState.PAUSED -> updateState(MediaState.Paused)
                PlayerState.DEFAULT -> updateState(MediaState.Default)
            }
            listener(newState)
        }
    }

    override fun preparePlayer() {
        playerRepository.preparePlayer()
    }

    override fun play() {
        playerRepository.play()
    }

    override fun pause() {
        playerRepository.pause()
    }

    override fun releasePlayer() {
        playerRepository.release()
    }

    override fun currentPosition(): Int {
        return playerRepository.getCurrentPosition()
    }

    override fun isPlaying(): Boolean {
        return playerRepository.isPlaying()
    }

    override fun loadTrack(): Track? {
        return playerRepository.sendTrack()
    }

    override fun onMediaStateListener(mediaState: MediaState) {
        this.mediaState = mediaState
    }

    fun setMediaStateListener(listener: MediaStateListener) {
        mediaStateListener = listener
    }

    private fun updateMediaState(mediaState: MediaState) {
        mediaStateListener?.onMediaStateListener(mediaState)
    }
}