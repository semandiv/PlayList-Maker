package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.SharedPrefRepository
import com.example.playlistmaker.domain.models.Track

class HistoryInteractorImpl(private val sharedPrefRepository: SharedPrefRepository): HistoryInteractor {
    override fun addTrack(track: Track) {
        sharedPrefRepository.addTrack(track)
    }

    override fun clearTrack() {
        sharedPrefRepository.clearTrack()
    }

    override fun getTrack(): MutableList<Track> {
        return sharedPrefRepository.getTrack()
    }
}