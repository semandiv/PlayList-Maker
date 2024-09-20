package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.PlayerRepository
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerStateListener
import com.example.playlistmaker.domain.models.PlayerState

class PlayerInteractorImpl (private val playerRepository: PlayerRepository) : PlayerInteractor{

    override fun play() {
        playerRepository.play()
    }

    override fun pause() {
        playerRepository.pause()
    }

    override fun releasePlayer() {
        playerRepository.release()
    }

    override fun observePlayerState(listener: PlayerStateListener){
        playerRepository.setPlayerStateListener(listener)
    }

    override fun currentPosition(): Int {
        return playerRepository.getCurrentPosition()
    }
}