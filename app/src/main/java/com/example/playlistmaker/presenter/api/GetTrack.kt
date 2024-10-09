package com.example.playlistmaker.presenter.api

import com.example.playlistmaker.domain.models.TrackSearchResult

interface GetTrack {
    fun loadTrack(query: String, callback: () -> Unit): TrackSearchResult
    fun getTrack() : TrackSearchResult
}