package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.PlayerRepository
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerStateListener
import com.example.playlistmaker.domain.models.PlayerState

class PlayerInteractorImpl (private val playerRepository: PlayerRepository) : PlayerInteractor{
    private var playerStateListener: PlayerStateListener? = null

    override fun observePlayerState(listener: PlayerStateListener) {
        playerStateListener = listener
        playerRepository.setPlayerStateListener(object: PlayerStateListener{
            override fun onPlayerStateChanged(state: PlayerState) {
                playerStateListener?.onPlayerStateChanged(state)
            }
        })

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