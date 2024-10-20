package com.example.playlistmaker.search.data.dto

class TracksResponse(
    val results: List<TrackDTO>, resultCode: Int
): Response(resultCode)