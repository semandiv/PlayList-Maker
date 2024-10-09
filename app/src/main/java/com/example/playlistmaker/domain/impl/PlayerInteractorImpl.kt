package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.PlayerRepository
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.models.PlayerState

class PlayerInteractorImpl (private val playerRepository: PlayerRepository) : PlayerInteractor{
    private var playerStateListener: ((PlayerState) -> Unit)? = null

    override fun observePlayerState(listener: (PlayerState) -> Unit) {
        playerStateListener = listener
        playerRepository.setPlayerStateListener { state ->
            playerStateListener?.invoke(state)
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
}