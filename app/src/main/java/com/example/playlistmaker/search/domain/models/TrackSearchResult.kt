package com.example.playlistmaker.search.domain.models

sealed class TrackSearchResult{
    data class SearchResult(val tracks: List<Track>): TrackSearchResult()
    data object NoResult: TrackSearchResult()
    data object NetworkError: TrackSearchResult()
}