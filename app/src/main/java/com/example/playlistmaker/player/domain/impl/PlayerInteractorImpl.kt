package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.data.PlayerRepository
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.MediaState
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.search.domain.models.Track

class PlayerInteractorImpl (private val playerRepository: PlayerRepository) : PlayerInteractor {

    private var stateListener: (MediaState) -> Unit = {}

    override fun updateState(newState: MediaState) {
        stateListener.invoke(newState)
    }

    override fun observeMediaState(listener: (MediaState) -> Unit) {
        playerRepository.setPlayerStateListener { newState->
            when (newState) {
                PlayerState.PREPARED -> updateState(MediaState.Prepared)
                PlayerState.PLAYING -> updateState(MediaState.Playing)
                PlayerState.PAUSED -> updateState(MediaState.Paused)
                PlayerState.DEFAULT -> updateState(MediaState.Default)
            }
        }
        stateListener = listener
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
}