package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.TrackDTO

sealed class NetworkResult {
    data class Success(val data: List<TrackDTO>) : NetworkResult()
    data object EmptyResult: NetworkResult()
    data object Error : NetworkResult()
    data object NetworkException : NetworkResult()
}