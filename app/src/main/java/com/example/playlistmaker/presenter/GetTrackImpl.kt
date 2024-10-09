package com.example.playlistmaker.presenter

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.TrackSearchResult
import com.example.playlistmaker.presenter.api.GetTrack

class GetTrackImpl (private val interactor: TracksInteractor) : GetTrack {

    private var newTracks = TrackSearchResult(0, emptyList())

    override fun loadTrack(query: String, callback: () -> Unit): TrackSearchResult {
        interactor.searchTracks(query) { foundTracks ->
            this.newTracks = foundTracks
            callback()
        }
        return newTracks
    }

    override fun getTrack(): TrackSearchResult {
        return newTracks
    }

}