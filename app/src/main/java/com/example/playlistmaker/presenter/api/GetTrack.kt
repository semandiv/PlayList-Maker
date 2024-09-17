package com.example.playlistmaker.presenter.api

import com.example.playlistmaker.domain.models.Track

interface GetTrack {
    fun getTrack() : Pair<Int, List<Track>>
}