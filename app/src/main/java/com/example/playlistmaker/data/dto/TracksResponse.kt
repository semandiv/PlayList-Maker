package com.example.playlistmaker.data.dto

class TracksResponse(
    val results: List<TrackDTO>, resultCode: Int
): Response(resultCode)