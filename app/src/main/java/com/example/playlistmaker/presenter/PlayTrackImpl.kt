package com.example.playlistmaker.presenter

import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerStateListener
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.presenter.api.PlayTrack

class PlayTrackImpl(private val previewUrl: String) : PlayTrack {
    private var playerState : PlayerState = PlayerState.DEFAULT
    private lateinit var playerInteractor: PlayerInteractor

    override fun preparePlayer(){
        if (previewUrl.isNotEmpty()) {
            playerInteractor = Creator.providePlayerInteractor(previewUrl)
            playerInteractor.observePlayerState(object : PlayerStateListener {
                override fun onPlayerStateChanged(state: PlayerState) {
                    playerState = state
                }
            })
            playerInteractor.preparePlayer()

        }
    }

    override fun play() {
        if (previewUrl.isNotEmpty()) {
            playerInteractor.play()
        }
    }

    override fun pause() {
        if (previewUrl.isNotEmpty()) {
            playerInteractor.pause()
        }
    }

    override fun releasePlayer() {
        if (previewUrl.isNotEmpty()) {
            playerInteractor.releasePlayer()
        }
    }

    override fun getPlayerState() = playerState

    override fun currentPosition(): Int {
        if (previewUrl.isNotEmpty()) {
            return playerInteractor.currentPosition()
        } else {
            return 0
        }
    }
}