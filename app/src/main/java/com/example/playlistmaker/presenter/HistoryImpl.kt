package com.example.playlistmaker.presenter

import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presenter.api.History

class HistoryImpl(private val historyInteractor: HistoryInteractor): History {
    override fun addTrack(track: Track) {
        historyInteractor.addTrack(track)
    }

    override fun getTracks(): MutableList<Track> {
        return historyInteractor.getTrack()
    }

    override fun clearHistory() {
        historyInteractor.clearTrack()
    }
}