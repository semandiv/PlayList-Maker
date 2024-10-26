package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.api.SharedPrefRepository
import com.example.playlistmaker.search.domain.models.Track

class HistoryInteractorImpl(private val sharedPrefRepository: SharedPrefRepository):
    HistoryInteractor {
    override fun addTrack(track: Track) {
        sharedPrefRepository.addTrack(track)
    }

    override fun clearTrack() {
        sharedPrefRepository.clearHistory()
    }

    override fun getTrack(): List<Track> {
        return sharedPrefRepository.getTrack()
    }
}