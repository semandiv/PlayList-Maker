package com.example.playlistmaker.presenter

import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presenter.api.GetTrack

class GetTrackImpl : GetTrack {
    private val interactor = Creator.provideTracksInteractor()
    private var newTracks: Pair<Int, List<Track>> = Pair(0, emptyList())

    fun loadTrack(query: String, callback: () -> Unit): Pair<Int, List<Track>> {
        interactor.searchTracks(query,
            object : TracksInteractor.TrackConsumer {
                override fun consume(foundTracks: Pair<Int, List<Track>>) {
                        this@GetTrackImpl.newTracks = foundTracks
                    callback()
                }
            })
        return newTracks
    }

    override fun getTrack(): Pair<Int, List<Track>> {
        return newTracks
    }

}