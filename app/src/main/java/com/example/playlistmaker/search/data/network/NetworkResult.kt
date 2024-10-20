package com.example.playlistmaker.search.data.network

sealed class NetworkResult<T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val code: Int) : NetworkResult<T>()
    class NetworkException<T>(val exception: Exception) : NetworkResult<T>()
}