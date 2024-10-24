package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.data.PlayerRepository
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.search.domain.models.Track

class PlayerInteractorImpl (private val playerRepository: PlayerRepository) : PlayerInteractor {

    override fun observePlayerState(listener: (PlayerState) -> Unit) {
        playerRepository.setPlayerStateListener(listener)
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