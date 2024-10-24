package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.ToPlayerInteractor
import com.example.playlistmaker.search.domain.api.TrackPlayRepository
import com.example.playlistmaker.search.domain.models.Track

class ToPlayerInteractorImpl(private val trackRepository: TrackPlayRepository): ToPlayerInteractor {
    override fun toPlayer(track: Track) {
        trackRepository.setTrackToPlay(track)
    }

}