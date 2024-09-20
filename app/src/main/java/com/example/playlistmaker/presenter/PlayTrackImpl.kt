package com.example.playlistmaker.presenter

import com.example.playlistmaker.Creator
import com.example.playlistmaker.presenter.api.PlayTrack

class PlayTrackImpl(private val previewUrl: String): PlayTrack {
    private val playerInteractor = Creator.providePlayerInteractor(previewUrl)

    override fun play() {
        playerInteractor.play()
    }

    override fun pause() {
        playerInteractor.pause()
    }

    override fun releasePlayer() {
        playerInteractor.releasePlayer()
    }
}